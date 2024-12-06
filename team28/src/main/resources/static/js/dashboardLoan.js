// binds the action and instrument id to the hidden input fields in the form so they're passed to the dto
function setAction(button) {
    const action = button.getAttribute('data-action');
    const instrumentId = button.getAttribute('data-id');

    if (action === "loan") {
        document.getElementById('loanAction').value = action;
        document.getElementById('loanInstrumentId').value = instrumentId;
    } else if (action === "return") {
        document.getElementById('returnAction').value = action;
        document.getElementById('returnInstrumentId').value = instrumentId;
    }
}