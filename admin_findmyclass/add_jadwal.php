<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Class Schedule Form</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">
    <style>
        *{
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        .formJadwal{
            background-color : #f8e505;
            justify-content : center;
            height: 100vh;
        }

        .btn btn-primary{
            display : block;
        }

        .classData{
            height: 100vh;
        }

        .sched_collection h2{
            color: #f8e505;
        }

        .sched_collection button{
            background-color: #f8e505;
        }

        .sched_collection button:hover{
            background-color: #ffc107;
        }
    </style>
</head>
<body>
    <div class="row gx-0">
        <div class="col-md-6">
            <form id="jadwalForm"class="formJadwal row p-4">
                <h2 class="text-center mb-3 text-primary">Daftar Jadwal Ruangan</h2>
                <div class="col-md-6">
                    <label for="namaRuang" class="form-label ">Kode Kelas:</label>
                    <input type="text" name="namaRuang" id="namaRuang" class="form-control" required>
                </div>
                <div class="col-md-6">
                    <label for="hari" class=" form-label">Hari:</label>
                    <select id="hari" name="hari" class="form-control" required>
                        <option value="Senin">Senin</option>
                        <option value="Selasa">Selasa</option>
                        <option value="Rabu">Rabu</option>
                        <option value="Kamis">Kamis</option>
                        <option value="Jumat">Jumat</option>
                    </select>
                </div>

                <div class="col-md-12">
                    <label for="matkul" class="form-label">Mata Kuliah:</label>
                    <input type="text" name="matkul" id="matkul" class="form-control" placeholder="Software Engineering" required>
                </div>
                

                <div class="col-md-6">
                    <label for="waktu" class="form-label">Waktu :</label>
                    <div class="form-check">
                        <input type="checkbox" id="waktu_1" name="waktu[]" value="08.00 - 08.50">
                        <label for="waktu_1" class="form-label">08.00 - 08.50</label>
                    </div>
                    <div class="form-check">
                        <input type="checkbox" id="waktu_2" name="waktu[]" value="08.50 - 09.40">
                        <label for="waktu_2" class="form-label">08.50 - 09.40</label>
                    </div>
                    <div class="form-check">
                        <input type="checkbox" id="waktu_3" name="waktu[]" value="09.40 - 10.30">
                        <label for="waktu_3" class="form-label">09.40 - 10.30</label>
                    </div>
                    <div class="form-check">
                        <input type="checkbox" id="waktu_4" name="waktu[]" value="10.30 - 11.20">
                        <label for="waktu_4" class="form-label">10.30 - 11.20</label>
                    </div>
                    <div class="form-check">
                        <input type="checkbox" id="waktu_5" name="waktu[]" value="11.20 - 12.10">
                        <label for="waktu_5" class="form-label">11.20 - 12.10</label>
                    </div>
                    <div class="form-check">
                        <input type="checkbox" id="waktu_6" name="waktu[]" value="12.10 - 13.00">
                        <label for="waktu_6" class="form-label">12.10 - 13.00</label>
                    </div>
                    <div class="form-check">
                        <input type="checkbox" id="waktu_7" name="waktu[]" value="13.00 - 13.50">
                        <label for="waktu_7" class="form-label">13.00 - 13.50</label>
                    </div>
                    <div class="form-check">
                        <input type="checkbox" id="waktu_8" name="waktu[]" value="13.50 - 14.40">
                        <label for="waktu_8" class="form-label">13.50 - 14.40</label>
                    </div>
                    <div class="form-check">
                        <input type="checkbox" id="waktu_9" name="waktu[]" value="14.40 - 15.30">
                        <label for="waktu_9" class="form-label">14.40 - 15.30</label>
                    </div>
                    <div class="form-check">
                        <input type="checkbox" id="waktu_10" name="waktu[]" value="15.30 - 16.20">
                        <label for="waktu_10" class="form-label">15.30 - 16.20</label>
                    </div>
                </div>

                <div class="col-md-3">
                    <label for="kom" class=" form-label">Kom:</label>
                    <select id="kom" name="kom" class="form-control" required>
                        <option value="A">A</option>
                        <option value="B">B</option>
                        <option value="C">C</option>
                    </select>
                </div>
                <div class="col-md-3">
                    <label for="stambuk" class=" form-label">Stambuk:</label>
                    <input type="text" name="stambuk" id="stambuk" class="form-control" placeholder="2021" required>
                </div>

                
                <input type="submit" value="Submit" class="btn btn-primary mt-2">
            </form>  
        </div>
        <div class="col-md-6">
            <!-- Display data -->
            <div id="classroomData" class="classData bg-primary">
                <div class="sched_collection p-4">
                    <h2 class="text-center mb-3">Schedule Registred</h2>
                    <div class="btn-group">
                        <button type="button" class="btn dropdown-toggle " data-bs-toggle="dropdown" aria-expanded="false">
                            Urutkan berdasarkan
                        </button>
                        <ul class="dropdown-menu">
                            <li><a class="dropdown-item" href="#">Hari</a></li>
                            <li><a class="dropdown-item" href="#">Kelas</a></li>
                        </ul>
                    </div>
                    <div id="scheduleContainer" class="overflow-auto mt-3">
                        <!-- Schedule data will be dynamically added here -->
                    </div>
                </div>
            </div>
        </div>
    <script type="module">
        // Import the functions you need from the SDKs you need
        import { initializeApp } from "https://www.gstatic.com/firebasejs/10.7.1/firebase-app.js";
        import { getDatabase, ref, push, onValue } from "https://www.gstatic.com/firebasejs/10.7.1/firebase-database.js";

        // TODO: Add SDKs for Firebase products that you want to use
        // https://firebase.google.com/docs/web/setup#available-libraries

        // Your web app's Firebase configuration
        // For Firebase JS SDK v7.20.0 and later, measurementId is optional
        const firebaseConfig = {
            apiKey: "AIzaSyAGQ6CUM9ik5KcYeMe3zROC2tC0G06i75Y",
            authDomain: "findmyclassjava.firebaseapp.com",
            databaseURL: "https://findmyclassjava-default-rtdb.asia-southeast1.firebasedatabase.app",
            projectId: "findmyclassjava",
            storageBucket: "findmyclassjava.appspot.com",
            messagingSenderId: "362186543525",
            appId: "1:362186543525:web:ff24f5cd22d95ceb73d84c",
            measurementId: "G-4W36PE8TMW"
        };

        // Initialize Firebase
        const app = initializeApp(firebaseConfig);
        const database = getDatabase();

        const jadwalForm = document.getElementById('jadwalForm');
        const classroomDataDiv = document.getElementById('classroomData');

        jadwalForm.addEventListener('submit',(e) => {

            e.preventDefault(); // Prevent form submission
            
            // Get form data
            var namaRuang = document.getElementById("namaRuang").value;
            var hari = document.getElementById("hari").value;
            var matkul = document.getElementById("matkul").value;
            var kom = document.getElementById("kom").value;
            var stambuk = document.getElementById("stambuk").value;
            var status = "Terisi";

            // Get selected time slots
            var selectedTimeSlots = [];
            var checkboxes = document.getElementsByName("waktu[]");
            checkboxes.forEach(function(checkbox) {
            if (checkbox.checked) {
                selectedTimeSlots.push(checkbox.value);
            }
            });

            var jam = mergeTimeSlots(selectedTimeSlots);

            // Create JSON object
            var formData = {
                namaRuang: namaRuang,
                hari: hari,
                matkul: matkul,
                jam: jam,
                status: status,
                kom: kom,
                stambuk: stambuk
            };
            
            // Push data to the database
            push(ref(database, 'classroom'), formData)
            .then(() => {
                console.log("Data sent to Firebase!");
                document.getElementById("jadwalForm").reset();
            })
            .catch((error) => {
                console.error("Error sending data to Firebase: ", error);
            });

            // Display data after form submission
            // displayClassroomData();

        })

        // Function to display classroom data
        // function displayClassroomData() {
        //     // Reference to the 'classroom' node in the database
        //     const classroomRef = ref(database, 'classroom');

        //     // Listen for changes to the data
        //     onValue(classroomRef, (snapshot) => {
        //         const data = snapshot.val();
        //         // Clear previous data
        //         classroomDataDiv.innerHTML = '';

        //         // Iterate over each entry and display it
        //         for (const key in data) {
        //             if (data.hasOwnProperty(key)) {
        //                 const entry = data[key];
        //                 classroomDataDiv.innerHTML += `<p>${entry.classCode}, ${entry.day}, ${entry.courseName}, ${entry.time}</p>`;
        //             }
        //         }
        //     });
        // }

        // Function to display classroom data
        function displayClassroomData(sortBy) {
            // Reference to the 'classroom' node in the database
            const classroomRef = ref(database, 'classroom');

            // Listen for changes to the data
            onValue(classroomRef, (snapshot) => {
                const data = snapshot.val();
                // Clear previous data
                document.getElementById('scheduleContainer').innerHTML = '';

                // Sort data based on user selection
                const sortedData = sortBy === 'classCode'
                    ? sortDataByClassCode(data)
                    : sortDataByDay(data);

                // Iterate over each entry and display it
                for (const key in sortedData) {
                    if (sortedData.hasOwnProperty(key)) {
                        const entry = sortedData[key];
                        const scheduleCard = createScheduleCard(entry);
                        document.getElementById('scheduleContainer').appendChild(scheduleCard);
                    }
                }
            });
        }

        // Function to sort data by class code
        function sortDataByClassCode(data) {
            // Sort data by class code
            const sortedData = {};
            for (const key in data) {
                if (data.hasOwnProperty(key)) {
                    const classCode = data[key].classCode;
                    if (!sortedData[classCode]) {
                        sortedData[classCode] = [];
                    }
                    sortedData[classCode].push(data[key]);
                }
            }
            return sortedData;
        }

        // Function to sort data by day
        function sortDataByDay(data) {
            // Sort data by day
            const sortedData = {
                'Senin': [],
                'Selasa': [],
                'Rabu': [],
                'Kamis': [],
                'Jumat': []
            };
            for (const key in data) {
                if (data.hasOwnProperty(key)) {
                    const day = data[key].day;
                    sortedData[day].push(data[key]);
                }
            }
            return sortedData;
        }

        // Function to create a schedule card
        function createScheduleCard(entry) {
            const card = document.createElement('div');
            card.className = 'card text-white bg-warning mb-3';
            card.style = 'max-width: 18rem;';

            const cardBody = document.createElement('div');
            cardBody.className = 'card-body';

            const cardTitle = document.createElement('h5');
            cardTitle.className = 'card-title';
            cardTitle.textContent = entry.courseName;

            const cardText = document.createElement('p');
            cardText.className = 'card-text';
            cardText.textContent = `Day: ${entry.day}, Time: ${entry.time}`;

            cardBody.appendChild(cardTitle);
            cardBody.appendChild(cardText);
            card.appendChild(cardBody);

            return card;
        }

        // Function to handle sorting by class code
        function sortByClassCode() {
            displayClassroomData('classCode');
        }

        // Function to handle sorting by day
        function sortByDay() {
            displayClassroomData('day');
        }

        // Initial display (default sorting)
        displayClassroomData('classCode');

        function mergeTimeSlots(timeSlots) {
            if (timeSlots.length === 0) {
                return "";
            }

            timeSlots.sort();

            var startTime = timeSlots[0].split(" - ")[0];
            var endTime = timeSlots[timeSlots.length - 1].split(" - ")[1];

            return startTime + " - " + endTime;
        }

    </script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-kenU1KFdBIe4zVF0s0G1M5b4hcpxyD9F7jL+jjXkk+Q2h455rYXK/7HAuoJl+0I4" crossorigin="anonymous"></script>

</body>
</html>

