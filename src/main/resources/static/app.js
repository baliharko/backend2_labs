"use strict";

const depositBtn = document.querySelector("#deposit-btn");
const withdrawBtn = document.querySelector("#withdraw-btn");

depositBtn.addEventListener("click", () => {
    const holder = document.querySelector("#holder").value;
    const amount = document.querySelector("#amount").value;

    transaction(holder, amount, transactionRequest.DEPOSIT);
});

withdrawBtn.addEventListener("click", () => {
    const holder = document.querySelector("#holder").value;
    const amount = document.querySelector("#amount").value;

    transaction(holder, amount, transactionRequest.WITHDRAW);
});

const transactionRequest = {
    DEPOSIT: "deposit",
    WITHDRAW: "withdraw"
}

const transaction = (holder, amount, transactionRequest) => {
    fetch(`http://localhost:8080/${transactionRequest}`, {
        method: "post",
        headers: {
            Accept: "application/json",
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            holder: holder,
            amount: amount,
        }),
    })
        .then((data) => data.json())
        .then((json) => {

            if(!json.holder)
                openAccount(holder);
            else
                updateInfo(json.holder, json.balance);
        })
        .catch((error) => console.log(error));
};

const openAccount = (holder) => {
    fetch("http://localhost:8080/openaccount", {
        method: "post",
        headers: {
            Accept: "application/json",
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            holder: holder,
        }),
    })
        .then((data) => data.json())
        .then((json) => {
            updateInfo('New account opened for ' + json.holder, json.balance);
        })
        .catch((error) => console.log(error));
};

const updateInfo = (holder, balance) => {
    const info = document.querySelector("#show-info");
    info.innerText = `${holder}: ${balance} kr`;
};
