const checkbox = document.getElementById("addChildAccount")
const addedFields = document.getElementById("childAccountRegistration")

checkbox.addEventListener("change", () => {
    if (checkbox.checked){
        addedFields.classList.remove("hidden")
    } else {
        addedFields.classList.add("hidden")
    }
})