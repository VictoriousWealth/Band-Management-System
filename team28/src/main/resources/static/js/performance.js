function showCreatePerformanceForm() {
    document.getElementById('createPerformanceForm').style.display = 'block';
}

function hideCreatePerformanceForm() {
    document.getElementById('createPerformanceForm').style.display = 'none';
}

function submitPerformance() {
    const name = document.getElementById('name').value;
    const venue = document.getElementById('venue').value;
    const dateTime = document.getElementById('dateTime').value;
    const band = document.querySelector('input[name="band"]:checked')?.value;
    const playlist = Array.from(document.getElementById('playlist').selectedOptions).map(option => option.value);

    if (!name || !venue || !dateTime || !band) {
        alert('Please fill all fields.');
        return;
    }

    // Submit the performance data to the backend
    fetch('/performances', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ name, venue, dateTime, band, playlist })
    })
    .then(response => {
        if (response.ok) {
            alert('Performance created successfully!');
            hideCreatePerformanceForm();
            // Refresh the list
        } else {
            alert('Failed to create performance.');
        }
    })
    .catch(error => console.error('Error:', error));
}

document.addEventListener('DOMContentLoaded', () => {
    fetch('/performances')
        .then(response => response.json())
        .then(data => {
            console.log('Parsed data:', data);
            const performanceTableBody = document.querySelector('#performanceTable tbody');
            performanceTableBody.innerHTML = ''; // Clear any existing items

            if (data.length === 0) {
                // Show a message if there are no performances
                const li = document.createElement('li');
                li.className = 'list-group-item';
                li.textContent = 'No performances available.';
                performanceList.appendChild(li);
            } else {
                // Populate the list with performances
                data.forEach(performance => {
                    const row = document.createElement('tr');

                    // Format date and time
                    const date = new Date(performance.dateTime);
                    const formattedDate = date.toLocaleDateString('en-US', {
                        year: 'numeric',
                        month: 'long',
                        day: 'numeric',
                    });
                    const formattedTime = date.toLocaleTimeString('en-US', {
                        hour: '2-digit',
                        minute: '2-digit',
                        hour12: true,
                    });

                    row.innerHTML = `
                        <td>${performance.name}</td>
                        <td>${performance.venue}</td>
                        <td>${formattedDate}, ${formattedTime}</td>
                        <td>${performance.band}</td>
                        <td>${performance.playlist && performance.playlist.length > 0 ? performance.playlist.join(', ') : 'No playlist'}</td>
                    `;
                    row.addEventListener('click', () => {
                    viewPerformanceDetails(performance.id);
                    });
                    performanceTableBody.appendChild(row);
                    });
            }
        })
        .catch(error => console.error('Error fetching performances:', error));
});

function viewPerformanceDetails(performanceId) {
    fetch(`/performances/${performanceId}/players`)
        .then(response => response.json())
        .then(players => {
            const playerList = document.getElementById('playerList');
            playerList.innerHTML = '';

            players.forEach(player => {
                const li = document.createElement('li');
                li.className = 'list-group-item';
                li.textContent = player.name;
                playerList.appendChild(li);
            });

            $('#performanceModal').modal('show');
        })
        .catch(error => console.error('Error fetching performance details:', error));
}