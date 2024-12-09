function populateEditForm(button) {
    const id = button.getAttribute("data-id");
    const row = button.closest("tr");
    const cells = row.getElementsByTagName("td");

    document.getElementById("editInstrumentId").value = id;
    document.getElementById("editInstrumentInput").value = cells[0].innerText;
    document.getElementById("editMake").value = cells[1].innerText;
    document.getElementById("editSerialNumber").value = cells[2].innerText;
    document.getElementById("editNote").value = cells[4].innerText;

    const inStorage = cells[3].querySelector(".badge-success") ? "true" : "false";
    document.getElementById("editInStorageYes").checked = inStorage === "true";
    document.getElementById("editInStorageNo").checked = inStorage === "false";

    document.getElementById("deleteInstrumentId").value = id;
}
