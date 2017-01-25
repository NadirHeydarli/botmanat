package com.botmanat.controller;

import com.botmanat.config.app.KeySingleton;
import com.botmanat.fb.Entry;
import com.botmanat.fb.ReceivedMessage;
import com.botmanat.model.DailyCurrency;
import com.botmanat.model.OfyHelper;
import com.botmanat.model.Subscriber;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.util.Lists;
import com.google.api.client.util.Maps;
import com.google.common.base.Strings;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Ints;
import lombok.extern.java.Log;
import org.apache.commons.codec.binary.Hex;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;


@Log
public class WebHookController {

    HttpServletRequest request;

    public WebHookController(HttpServletRequest request) {
        this.request = request;
    }

    public Response verifyWebHook(String hubMode, String verifyToken, String challenge) {

        String validationToke = KeySingleton.getInstance().getWebhookValidationToken();

        if (Strings.isNullOrEmpty(hubMode)) {
            log.severe("hub mode is null");
        } else {
            if (hubMode.equals("subscribe") && validationToke.equals(verifyToken)) {
                log.warning("Validation OK");
                return Response.ok(challenge).build();

            } else {
                log.severe(hubMode + "=hubMode");
                log.severe(verifyToken + "=verifyToken");
            }
        }

        log.severe("Failed validation. Make sure the validation tokens match.");
        return Response.status(403).build();

    }


    public Response listener(String jsonString) {
        if (!checkSum(jsonString)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            ReceivedMessage receivedMessage = mapper.readValue(jsonString, ReceivedMessage.class);
            List<Entry> entries = receivedMessage.getEntry();
            for (Entry entry : entries) {
                List<Entry.Messaging> messagingList = entry.getMessaging();
                for (Entry.Messaging messaging : messagingList) {
                    if (messaging.isPostBack()) {

                        //quick replies don't have their message type set to postback
                        // and have postback inside the message object
                        Entry.Postback postback = messaging.getPostback();
                        String payload;
                        if (postback == null) {
                            payload = messaging.getMessage().getQuick_reply().getPayload();
                        } else {
                            payload = postback.getPayload();
                        }

                        MessageProcessor.process(messaging.getSender().getId(), payload);
                    } else if (messaging.isTextMessage()) {
                        MessageProcessor.process(messaging.getSender().getId(), messaging.getMessage().getText());
                    }
                }
            }
        } catch (Exception e) {
            log.severe(e.getMessage());
        }

        return Response.ok().build();
    }

    private boolean checkSum(String jsonString) {
        String appTokenFromDevelopersPage = KeySingleton.getInstance().getAppTokenFromFBDevelopersPage();
        String facebookSignedChecksum = request.getHeader("X-Hub-Signature");

        if (facebookSignedChecksum == null || facebookSignedChecksum.split("=").length != 2) {
            return false;
        }

        try {
            Mac hmac = Mac.getInstance("HmacSHA1");
            hmac.init(new SecretKeySpec(appTokenFromDevelopersPage.getBytes(Charset.forName("UTF-8")), "HmacSHA1"));
            String calculatedSignature = new String(Hex.encodeHex(hmac.doFinal(jsonString.getBytes(Charset.forName("UTF-8")))));
            return facebookSignedChecksum.split("=")[1].equals(calculatedSignature);

        } catch (NoSuchAlgorithmException e) {
            log.severe("Unable to checksum NoSuchAlgorithmException " + e.getMessage());
        } catch (InvalidKeyException e) {
            log.severe("Unable to checksum InvalidKeyException " + e.getMessage());
        }

        return false;
    }

    public Response dailyCurrencyFetch() {

        LocalDate today = LocalDate.now();
        if (!dailyCheckerShouldRunToday(today)) {
            return Response.ok().build();
        }

        URLConnection urlConnection = getUrlConnection(today);

        if (urlConnection == null) {
            return Response.ok().build();
        }

        Document xmlDocument = getXMLDocument(urlConnection);

        if (xmlDocument == null) {
            return Response.ok().build();
        }

        LocalDate xmlFileDate = parseDate(xmlDocument);

        if (!xmlFileDate.equals(today)) {
            return Response.ok().build();
        }

        notifySubscribers(extractDailyCurrency(xmlFileDate, xmlDocument), CurrencyHelper.getPenultimativeCurrency());
        return Response.ok().build();
    }

    private void notifySubscribers(DailyCurrency dailyCurrency, DailyCurrency previousCurrency) {
        List<Subscriber> subscribers = OfyHelper.get(Subscriber.class).list();
        for (Subscriber subscriber : subscribers) {
            if (subscriber.getSubscribedCurrencies() != null && !subscriber.getSubscribedCurrencies().isEmpty()) {
                String prettyStringFor = CurrencyHelper.getPrettyStringFor(previousCurrency, dailyCurrency, subscriber.getSubscribedCurrencies());
                Sender.sendTextMessage(prettyStringFor, subscriber.getSenderID());
            }
        }
    }

    private URLConnection getUrlConnection(LocalDate today) {
        return URLHelper.getUrlConnection("http://www.cbar.az/currencies/" + today.toString("dd.MM.YYYY") + ".xml");
    }

    private boolean dailyCheckerShouldRunToday(LocalDate today) {
        if (today.getDayOfWeek() == DateTimeConstants.SATURDAY || today.getDayOfWeek() == DateTimeConstants.SUNDAY) {
            return false;
        }

        return OfyHelper.get(DailyCurrency.class).filter("date", today).count() == 0;

    }

    private Document getXMLDocument(URLConnection con) {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        Document doc = null;

        try {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(con.getInputStream());
            doc.getDocumentElement().normalize();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            log.severe("Unable to fetch xml document from cbar " + e.getMessage());
        }

        return doc;
    }

    private DailyCurrency extractDailyCurrency(LocalDate date, Document doc) {
        NodeList currencyList = doc.getFirstChild().getFirstChild().getChildNodes();

        Map<String, DailyCurrency.Currency> currenciesMap = Maps.newHashMap();
        for (int i = 0; i < currencyList.getLength(); i++) {
            Element currency = (Element) currencyList.item(i);

            Integer nominalTryParse = Ints.tryParse(currency.getFirstChild().getTextContent());
            Double rateTryParse = Doubles.tryParse(currency.getLastChild().getTextContent());
            if (rateTryParse != null && nominalTryParse != null) {
                currenciesMap.put(currency.getAttribute("Code").toLowerCase(), new DailyCurrency.Currency(rateTryParse, nominalTryParse));
            } else {
                log.severe("Cannot parse " + currency.getTextContent());
            }
        }

        DailyCurrency dailyCurrency = new DailyCurrency(date, currenciesMap);
        OfyHelper.save(dailyCurrency);
        return dailyCurrency;
    }

    private LocalDate parseDate(Document xmlDocument) {
        String date = xmlDocument.getDocumentElement().getAttribute("Date");
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd.MM.yyyy");
        return formatter.parseLocalDate(date);
    }

    public Response plotData(String currency) {
        List<DailyCurrency> currencies = OfyHelper.get(DailyCurrency.class).order("-date").limit(30).list();

        List<Object[]> values = Lists.newArrayList();
        for (DailyCurrency dailyCurrency : currencies) {
            if (dailyCurrency.getCurrencies().containsKey(currency)) {
                LocalDate date = dailyCurrency.getDate();
                values.add(new Object[]{date.toDate().getTime(), dailyCurrency.getCurrencies().get(currency).getValue()});
            }
        }
        return Response.ok(values).build();
    }
}
