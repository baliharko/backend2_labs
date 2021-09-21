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
    WITHDRAW: "withdraw",
};

const transaction = (holder, amount, transactionRequest) => {

    let statusCode;
    let message;

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
        .then((data) => {
            statusCode = data.status;
            message = data.message;
            return data.json();
        })
        .then((json) => {

            if (statusCode) {
                if (statusCode == 501) {
                    console.log("TRANSACTION: in open account")
                    openAccount(holder);
                } else if (statusCode == 200) {
                    console.log("TRANSACTION: in update info 200")
                    updateInfo(json.holder, json.balance);
                } else {                    
                    updateInfo(json.message || message);
                }
                return;
            }

            console.log("TRANSACTION: outside json status")
            console.log(json);
            updateInfo(json.message);
        })
        .catch((error) => console.log(error));
};

const openAccount = (holder) => {

    let statusCode;
    let message;

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
        .then((data) => {
            statusCode = data.status;
            message = data.message;
            return data.json();
        })
        .then((json) => {

            console.log(statusCode);
            console.log(message);

            if (statusCode && statusCode == 200) 
                updateInfo("New account opened for " + json.holder, json.balance);
            else 
                updateInfo(json.message || message);
        })
        .catch((error) => console.log(error));
};

const updateInfo = (holder, balance) => {
    const info = document.querySelector("#show-info");
    info.innerText = balance ? `${holder}: ${balance} kr` : `${holder}`;
};
