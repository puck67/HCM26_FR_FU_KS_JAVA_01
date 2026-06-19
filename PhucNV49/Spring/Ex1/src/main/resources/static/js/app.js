class LmsApp {
    constructor() {
        this.apiBaseUrl = '/api';
        this.currentCourse = null; // Holds { courseCode, startDate } when managing lessons
        this.editingLessonId = null; // Holds lesson ID when editing a lesson
        this.deleteTargetLessonId = null; // Holds lesson ID temporarily for delete confirmation
    }

    init() {
        // Show default screen
        this.showScreen('screen-welcome');
        this.setBannerTitle('Banner');
    }

    /**
     * Switch view screen panels and update navigation items' active states
     */
    showScreen(screenId) {
        // Hide all screens
        document.querySelectorAll('.screen-panel').forEach(panel => {
            panel.classList.add('hidden');
        });

        // Hide alert container on screen changes
        this.hideAlert();

        // Show targets
        const targetScreen = document.getElementById(screenId);
        if (targetScreen) {
            targetScreen.classList.remove('hidden');
        }

        // Update Left Menu active state
        document.querySelectorAll('.nav-item').forEach(item => {
            item.classList.remove('active');
        });

        if (screenId === 'screen-course-form') {
            document.getElementById('nav-add-course').classList.add('active');
        } else if (screenId === 'screen-courses-list') {
            document.getElementById('nav-manage-courses').classList.add('active');
        } else if (screenId === 'screen-students-list') {
            document.getElementById('nav-manage-students').classList.add('active');
        }
    }

    setBannerTitle(title) {
        document.getElementById('banner-title').textContent = title;
    }

    showAlert(message, duration = 4000) {
        const container = document.getElementById('alert-container');
        const msgEl = document.getElementById('alert-message');
        msgEl.textContent = message;
        container.classList.remove('hidden');

        // Auto hide
        if (this.alertTimeout) clearTimeout(this.alertTimeout);
        this.alertTimeout = setTimeout(() => {
            this.hideAlert();
        }, duration);
    }

    hideAlert() {
        document.getElementById('alert-container').classList.add('hidden');
    }


    showAddCourse() {
        this.showScreen('screen-course-form');
        this.setBannerTitle('Banner');
        this.resetCourseForm();
    }

    async showManageCourses() {
        this.showScreen('screen-courses-list');
        this.setBannerTitle('Banner - Courses Management');
        await this.loadCoursesList();
    }

    showManageStudents() {
        this.showScreen('screen-students-list');
        this.setBannerTitle('Banner - Students Management');
    }

    async showLessonDetail(courseCode, startDate) {
        this.currentCourse = { courseCode, startDate };
        this.editingLessonId = null;

        // Reset lesson form
        this.resetLessonForm();

        // Update details summary card
        document.getElementById('summary-course-code').textContent = courseCode;
        document.getElementById('summary-start-date').textContent = startDate;
        document.getElementById('table-course-code').textContent = courseCode;

        // Change Banner title
        this.setBannerTitle('Banner - Lesson Detail');

        // Show screen
        this.showScreen('screen-lesson-detail');

        // Fetch and load lessons table
        await this.loadLessonsList();
    }

    resetCourseForm() {
        document.getElementById('course-form').reset();
        document.getElementById('lesson-detail-link-container').classList.add('hidden');
        this.hideAlert();
    }

    async saveCourse(event) {
        event.preventDefault();
        this.hideAlert();

        const form = event.target;
        const courseCode = form.courseCode.value.trim();
        const startDate = form.startDate.value.trim();
        const courseName = form.courseName.value.trim();
        const category = form.category.value;
        const instructor = form.instructor.value.trim();

        // Simple format check for start date (DD/MM/YYYY)
        const datePattern = /^\d{2}\/\d{2}\/\d{4}$/;
        if (!datePattern.test(startDate)) {
            alert("Vui lòng nhập Ngày khai giảng đúng định dạng DD/MM/YYYY (ví dụ: 15/10/2023)");
            return;
        }

        const payload = { courseCode, startDate, courseName, category, instructor };

        try {
            const response = await fetch(`${this.apiBaseUrl}/courses`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });

            const result = await response.json();

            if (response.ok && result.success) {
                this.showAlert("Add a new Course successfully!");
                
                // Show Lesson Detail dynamic link on the screen
                const linkContainer = document.getElementById('lesson-detail-link-container');
                const linkBtn = document.getElementById('btn-to-lesson-detail');
                
                linkBtn.onclick = (e) => {
                    e.preventDefault();
                    this.showLessonDetail(courseCode, startDate);
                };
                
                linkContainer.classList.remove('hidden');
            } else {
                alert("Error: " + (result.message || "Failed to save course"));
            }
        } catch (err) {
            console.error(err);
            alert("Network error: Failed to connect to server");
        }
    }

    async loadCoursesList() {
        const tbody = document.getElementById('courses-table-body');
        tbody.innerHTML = `<tr><td colspan="6" class="empty-table-message">Loading courses...</td></tr>`;

        try {
            const response = await fetch(`${this.apiBaseUrl}/courses`);
            const result = await response.json();

            if (response.ok && result.success) {
                const courses = result.data || [];
                if (courses.length === 0) {
                    tbody.innerHTML = `<tr><td colspan="6" class="empty-table-message">No courses found. Add a course first!</td></tr>`;
                    return;
                }

                tbody.innerHTML = courses.map(course => `
                    <tr>
                        <td><strong>${course.courseCode}</strong></td>
                        <td>${course.startDate}</td>
                        <td>${course.courseName}</td>
                        <td><span class="badge">${course.category}</span></td>
                        <td>${course.instructor}</td>
                        <td>
                            <a href="#" class="action-link" onclick="app.showLessonDetail('${course.courseCode}', '${course.startDate}')">
                                <i class="fa-solid fa-list-check"></i> Lessons
                            </a>
                        </td>
                    </tr>
                `).join('');
            } else {
                tbody.innerHTML = `<tr><td colspan="6" class="empty-table-message error-message">Failed to load courses: ${result.message}</td></tr>`;
            }
        } catch (err) {
            console.error(err);
            tbody.innerHTML = `<tr><td colspan="6" class="empty-table-message error-message">Network error loading courses</td></tr>`;
        }
    }

    resetLessonForm() {
        document.getElementById('lesson-form').reset();
        document.getElementById('lessonId').value = '';
        this.editingLessonId = null;
        
        // Reset save button text
        document.getElementById('btn-save-lesson').textContent = 'Save Lesson';
        document.getElementById('lesson-form-title').textContent = 'Add/Edit Lesson Detail';
    }

    async loadLessonsList() {
        if (!this.currentCourse) return;

        const { courseCode, startDate } = this.currentCourse;
        const tbody = document.getElementById('lessons-table-body');
        tbody.innerHTML = `<tr><td colspan="6" class="empty-table-message">Loading lessons...</td></tr>`;

        try {
            const url = `${this.apiBaseUrl}/lessons?courseCode=${encodeURIComponent(courseCode)}&startDate=${encodeURIComponent(startDate)}`;
            const response = await fetch(url);
            const result = await response.json();

            if (response.ok && result.success) {
                const lessons = result.data || [];
                this.renderLessonsTable(lessons);
            } else {
                tbody.innerHTML = `<tr><td colspan="6" class="empty-table-message error-message">Failed to load lessons: ${result.message}</td></tr>`;
            }
        } catch (err) {
            console.error(err);
            tbody.innerHTML = `<tr><td colspan="6" class="empty-table-message error-message">Network error loading lessons</td></tr>`;
        }
    }

    renderLessonsTable(lessons) {
        const tbody = document.getElementById('lessons-table-body');
        if (lessons.length === 0) {
            tbody.innerHTML = `<tr><td colspan="6" class="empty-table-message">No lessons found. Add one above!</td></tr>`;
            return;
        }

        tbody.innerHTML = lessons.map(lesson => `
            <tr id="lesson-row-${lesson.id}">
                <td>${lesson.id}</td>
                <td><strong>${lesson.lessonName}</strong></td>
                <td>${lesson.duration} mins</td>
                <td>${lesson.contentType}</td>
                <td><span class="status-badge status-${lesson.status === 'Đang mở' ? 'open' : lesson.status === 'Bản nháp' ? 'draft' : 'locked'}">${lesson.status}</span></td>
                <td>
                    <a href="#" class="action-link" onclick="app.editLesson(${lesson.id}, '${lesson.lessonName.replace(/'/g, "\\'")}', ${lesson.duration}, '${lesson.contentType}', '${lesson.status}')">Edit</a>
                    <span class="action-divider">/</span>
                    <a href="#" class="action-link text-danger" onclick="app.confirmDeleteLesson(${lesson.id})">Delete</a>
                </td>
            </tr>
        `).join('');
    }

    async saveLesson(event) {
        event.preventDefault();
        if (!this.currentCourse) return;

        const { courseCode, startDate } = this.currentCourse;
        const form = event.target;
        const lessonName = form.lessonName.value.trim();
        const duration = parseInt(form.duration.value);
        const contentType = form.contentType.value;
        const status = form.status.value;

        const payload = { lessonName, duration, contentType, status };

        try {
            let response;
            let successMessage;
            
            if (this.editingLessonId) {
                // Update mode
                response = await fetch(`${this.apiBaseUrl}/lessons/${this.editingLessonId}`, {
                    method: 'PUT',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(payload)
                });
                successMessage = "Lesson updated successfully!";
            } else {
                // Create mode
                const url = `${this.apiBaseUrl}/lessons?courseCode=${encodeURIComponent(courseCode)}&startDate=${encodeURIComponent(startDate)}`;
                response = await fetch(url, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(payload)
                });
                successMessage = "Add a new Lesson successfully!";
            }

            const result = await response.json();

            if (response.ok && result.success) {
                this.showAlert(successMessage);
                this.resetLessonForm();
                await this.loadLessonsList();
            } else {
                alert("Error: " + (result.message || "Failed to save lesson"));
            }
        } catch (err) {
            console.error(err);
            alert("Network error: Failed to connect to server");
        }
    }

    editLesson(id, lessonName, duration, contentType, status) {
        // Set state to editing
        this.editingLessonId = id;

        // Fill form fields
        document.getElementById('lessonId').value = id;
        document.getElementById('lessonName').value = lessonName;
        document.getElementById('duration').value = duration;
        document.getElementById('contentType').value = contentType;
        document.getElementById('status').value = status;

        // Change Title & Button Text
        document.getElementById('lesson-form-title').textContent = 'Edit Lesson Detail';
        document.getElementById('btn-save-lesson').textContent = 'Save Lesson';

        // Scroll form card into view
        document.getElementById('lesson-form').scrollIntoView({ behavior: 'smooth' });
    }

    confirmDeleteLesson(id) {
        this.deleteTargetLessonId = id;
        
        // Show custom modal overlay
        const modal = document.getElementById('confirm-modal');
        modal.classList.remove('hidden');

        // Bind Yes/No buttons
        document.getElementById('btn-modal-yes').onclick = async () => {
            await this.executeDeleteLesson();
            this.closeDeleteModal();
        };

        document.getElementById('btn-modal-no').onclick = () => {
            this.closeDeleteModal();
        };
    }

    closeDeleteModal() {
        document.getElementById('confirm-modal').classList.add('hidden');
        this.deleteTargetLessonId = null;
    }

    async executeDeleteLesson() {
        if (!this.deleteTargetLessonId) return;

        try {
            const response = await fetch(`${this.apiBaseUrl}/lessons/${this.deleteTargetLessonId}`, {
                method: 'DELETE'
            });

            const result = await response.json();

            if (response.ok && result.success) {
                this.showAlert("Lesson deleted successfully!");
                await this.loadLessonsList();
                
                // If the deleted lesson was currently being edited, reset the form
                if (this.editingLessonId === this.deleteTargetLessonId) {
                    this.resetLessonForm();
                }
            } else {
                alert("Error: " + (result.message || "Failed to delete lesson"));
            }
        } catch (err) {
            console.error(err);
            alert("Network error: Failed to connect to server");
        }
    }
}

// Instantiate and initialize the app
const app = new LmsApp();
document.addEventListener('DOMContentLoaded', () => {
    app.init();
});
window.app = app; // Expose to HTML inline event handlers
