/**
 * Nexus Academy - Integrated Microservices Logic
 * Gateway: http://localhost:8080
 */

const GATEWAY_URL = "http://localhost:8080";

const app = {
    // 1. AUTHENTICATION LOGIC (User-Service)
    login: async () => {
        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;
        const authBtn = document.querySelector('.main-btn');

        if (!username || !password) {
            alert("Please enter both ID and Access Key");
            return;
        }

        authBtn.innerText = "Authorizing...";
        authBtn.disabled = true;

        try {
            // Note: In your final build, this will hit your /auth/login endpoint
            // For the demo, we simulate a successful login check
            console.log(`Authenticating user: ${username}`);
            
            // Success Transition
            setTimeout(() => {
                document.getElementById('login-view').style.display = 'none';
                document.getElementById('dashboard-view').style.display = 'flex';
                document.getElementById('user-id-display').innerText = username;
                
                // Automatically fetch questions from DB 1 immediately after login
                app.loadQuestions();
            }, 800);

        } catch (error) {
            alert("Auth Service Unreachable. Check Gateway at 8080.");
            authBtn.innerText = "Authorize";
            authBtn.disabled = false;
        }
    },

    // 2. FETCH QUESTIONS FROM DB 1 (Exam-Service)
    loadQuestions: async () => {
        const container = document.getElementById('question-list');
        const intro = document.getElementById('exam-intro');
        const quizArea = document.getElementById('quiz-area');

        intro.style.display = 'none';
        quizArea.style.display = 'block';
        container.innerHTML = "<div class='mcq-box'><h3>Loading questions from MySQL...</h3></div>";

        try {
            // Fetching the 10 questions you inserted into the database
            const response = await fetch(`${GATEWAY_URL}/exams/101`);
            
            if (!response.ok) throw new Error("Exam-Service returned an error");
            
            const questions = await response.json();

            // Render all questions with 4 options (A, B, C, D)
            container.innerHTML = questions.map((q, index) => `
                <div class="mcq-box" data-id="${q.id}">
                    <p><strong>Q${index + 1}. ${q.content}</strong></p>
                    <div class="options-grid">
                        <label class="option-item">
                            <input type="radio" name="q${q.id}" value="A"> A. ${q.optiona}
                        </label>
                        <label class="option-item">
                            <input type="radio" name="q${q.id}" value="B"> B. ${q.optionb}
                        </label>
                        <label class="option-item">
                            <input type="radio" name="q${q.id}" value="C"> C. ${q.optionc}
                        </label>
                        <label class="option-item">
                            <input type="radio" name="q${q.id}" value="D"> D. ${q.optiond}
                        </label>
                    </div>
                </div>
            `).join('');

        } catch (error) {
            console.error(error);
            container.innerHTML = `
                <div class="mcq-box" style="border-color: #ef4444;">
                    <h3 style="color: #ef4444;">Connection Error</h3>
                    <p>Could not fetch questions from Exam-Service via Gateway (8080).</p>
                    <p>Check if Eureka, Gateway, and Exam-Service are all UP.</p>
                </div>`;
        }
    },

    // 3. SUBMIT & CALCULATE SCORE (Result-Service -> DB 3)
    submitExam: async () => {
        const submitBtn = document.querySelector('.submit-btn');
        const questionBoxes = document.querySelectorAll('.mcq-box');
        const responses = [];

        submitBtn.innerText = "Evaluating...";
        submitBtn.disabled = true;

        // Collect student answers from the UI
        questionBoxes.forEach(box => {
            const qId = box.getAttribute('data-id');
            const selected = box.querySelector(`input[name="q${qId}"]:checked`);
            
            if (qId) {
                responses.push({
                    "questionId": parseInt(qId),
                    "studentAnswer": selected ? selected.value : "" // Sends "A", "B", "C", or "D"
                });
            }
        });

        // The exact payload your Result-Service expects
        const payload = {
            "userId": 1, 
            "examId": "101",
            "responses": responses
        };

        try {
            // Send payload to Result-Service to calculate and store in DB 3
            const res = await fetch(`${GATEWAY_URL}/results/submit`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });

            if (!res.ok) throw new Error("Result-Service calculation failed");

            const resultData = await res.json();

            // Transition to Result View to show the score of 7 (or whatever was earned)
            document.getElementById('dashboard-view').style.display = 'none';
            document.getElementById('result-view').style.display = 'flex';
            
            // Update UI with REAL data from database
            document.getElementById('final-score').innerText = resultData.score;
            document.getElementById('result-status').innerText = `STATUS: ${resultData.status}`;

        } catch (error) {
            alert("Error: Result-Service is unavailable. Ensure your database connection is active.");
            submitBtn.innerText = "Submit to Result Service";
            submitBtn.disabled = false;
        }
    }
};