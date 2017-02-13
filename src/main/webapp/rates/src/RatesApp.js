import React, { Component } from 'react';
import './RatesApp.css';
const defaultCurrency = 'USD';
const defailtAction = 'SELL';

function ExchangeActionButtonList(props) {

    return ( < div
    className = "rateActionButtons" >
        < button
    className = {"button actionButton" +(props.displayedAction === 'SELL' ? " activeButton" : "")}
    onClick = {() =
>
    props.onClick('SELL')
}
>
Banks
Sell < / button >
< button
className = {"button actionButton" +(props.displayedAction === 'BUY' ? " activeButton" : "")}
onClick = {() =
>
props.onClick('BUY')
}
>
Banks
Buy < / button >
< / div >
)
;
}


function CurrencyButtonsList(props) {
    const currencies = [
        {name: 'USD', icon: "&#36;"},
        {name: 'EUR', icon: "&euro;"},
        {name: 'GBP', icon: "&euro;"},
        {name: 'RUB', icon: "&euro;"},
        {name: 'TRY', icon: "&euro;"}
    ];

    return (
        < div
    className = "rateCurrencyButtons" >
    {
        currencies.map(currency = > {
        return ( < button
        className = {"button" +(props.displayedCurrency === currency.name ? " activeButton" : "" )}
    key = {currency.name
}
onClick = {() =
>
props.onClick(currency.name)
}
>
{
    currency.name
}
</
button >
)
})
}
</
div >
)
;

}

class RatesView extends Component {

    constructor(props) {
        super(props);
        this.state = {
            ratesMap: null,
            displayedCurrency: defaultCurrency,
            displayedAction: defailtAction
        }

        this.onCurrencyButtonClick = this.onCurrencyButtonClick.bind(this);
        this.onExchangeActionButtonClick = this.onExchangeActionButtonClick.bind(this);
    }

    componentDidMount() {
        let self = this;
        var xmlhttp = new XMLHttpRequest();
        xmlhttp.responseType = "json";
        xmlhttp.onreadystatechange = function () {
            if (xmlhttp.readyState === XMLHttpRequest.DONE) {
                if (xmlhttp.status === 200) {
                    self.setState({
                        ratesMap: xmlhttp.response.ratesMap
                    });
                }
            }
        };

        xmlhttp.open("GET", "/webhook/exchange-rates", true);
        xmlhttp.send();
    }

    render() {
        return (
            < div >
            < ExchangeActionButtonList
        displayedAction = {this.state.displayedAction
    }

    onClick = {(displayedAction) =
>
        this
.
    onExchangeActionButtonClick(displayedAction)
}
></
ExchangeActionButtonList >
< div
className = "ratesBody" >
{
(this.state.ratesMap) ?
<
table >
< tbody > {this.getRatesList()
}</
tbody >
< / table >
: <
img
id = "spinner"
alt = "Loading..."
src = "../../spinner.gif" / >
}

</
div >
< CurrencyButtonsList
displayedCurrency = {this.state.displayedCurrency
}
onClick = {(displayedCurrency) =
>
this.onCurrencyButtonClick(displayedCurrency)
}
></
CurrencyButtonsList >
< / div >
)
;
}

getRatesList()
{
    const {ratesMap, displayedCurrency, displayedAction} = this.state;
    const currencyList = ratesMap[`${displayedCurrency}
    _$
    {
        displayedAction
    }`]
    ;

    return currencyList
            .sort((a, b) = > {
            if(a.rate < b.rate)
    {
        return this.state.displayedAction === 'SELL' ? -1 : 1;
    }

    if (a.rate > b.rate) {
        return this.state.displayedAction === 'SELL' ? 1 : -1;
    }

    return 0;

}
)
.
slice(0, 9)
    .map(el = > {
    return (
    < tr key = {el.bankName
}>
<
td
className = "bankName" > {el.bankName
}</
td >
< td
className = "bankRate" > {this.trimZeros(el.rate)
}</
td >
< / tr >
)
;
})
;
}

trimZeros(rate)
{
    let rateArray = rate.split('');
    for (let i = rateArray.length - 1; i >= 1; i--) {
        if (rateArray[i] === '0' || rateArray[i] === '.') {
            rateArray.pop();
        } else {
            break;
        }
    }

    return rateArray.join('');
}

onCurrencyButtonClick(displayedCurrency)
{
    if (displayedCurrency === this.state.displayedCurrency) {
        return;
    }

    this.setState({
        displayedCurrency
    });
}

onExchangeActionButtonClick(displayedAction)
{
    if (displayedAction === this.state.displayedAction) {
        return;
    }

    this.setState({
        displayedAction
    });
}
}

class RatesApp extends Component {
    render() {
        return (
            < div > < RatesView > < /RatesView></
        div >
    )
        ;
    }
}

export default RatesApp;
