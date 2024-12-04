// Listen for button clicks in the table
$('#loanModal, #returnModal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget); // The button that triggered the modal
    var instrumentId = button.data('id'); // Extract the instrumentId
    var action = button.data('action'); // Extract the action (loan or return)

    // Set the instrumentId in the form
    if (action === 'loan') {
        $('#loanInstrumentId').val(instrumentId);
    } else if (action === 'return') {
        $('#returnInstrumentId').val(instrumentId);
    }

    // Handle form submission
    var form = $(this).find('form');
    form.submit(function (e) {
        e.preventDefault();

        var memberName = $('#memberName').val(); // Get member name (if loan)
        var instrumentId = $('#loanInstrumentId').val() || $('#returnInstrumentId').val(); // Get the instrument ID

        var action = button.data('action'); // Get loan or return action
        var loanRequest = {
            instrumentId: instrumentId,
            action: action,
            memberName: memberName
        };

        // Make the POST request to the backend
        $.ajax({
            url: '/loans/loanAction', // Replace with your actual route
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(loanRequest),
            success: function (response) {
                // Close the modal and update the table as needed
                $('#loanModal').modal('hide');
                $('#returnModal').modal('hide');
                location.reload(); // Reload the page to reflect changes
            },
            error: function (xhr, status, error) {
                alert("There was an error: " + error);
            }
        });
    });
});