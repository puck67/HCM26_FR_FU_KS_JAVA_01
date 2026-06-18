document.addEventListener('DOMContentLoaded', () => {
    // Current Active Course & State
    let currentCourse = null;
    let editingLessonId = null;

    // DOM Elements
    const panels = {
        home: document.getElementById('panel-home'),
        addCourse: document.getElementById('panel-add-course'),
        coursesManage: document.getElementById('panel-courses-manage'),
        studentsManage: document.getElementById('panel-students-manage'),
        lessonDetail: document.getElementById('panel-lesson-detail')
    };

    const sidebarLinks = {
        addCourse: document.getElementById('link-add-course'),
        coursesManage: document.getElementById('link-courses-manage'),
        studentsManage: document.getElementById('link-students-manage')
    };

    // --- Navigation ---
    function switchPanel(activeKey) {
        // Hide all panels
        Object.keys(panels).forEach(key => {
            panels[key].classList.remove('active');
        });
        // Remove active class from sidebar links
        Object.keys(sidebarLinks).forEach(key => {
            sidebarLinks[key].classList.remove('active');
        });

        // Show active panel
        if (panels[activeKey]) {
            panels[activeKey].classList.add('active');
        }
        // Activate link in sidebar
        if (sidebarLinks[activeKey]) {
            sidebarLinks[activeKey].classList.add('active');
        }

        // Action when panel is shown
        if (activeKey === 'coursesManage') {
            loadCourses();
        }
    }

    // Bind sidebar events
    sidebarLinks.addCourse.addEventListener('click', (e) => {
        e.preventDefault();
        resetCourseForm();
        switchPanel('addCourse');
    });

    sidebarLinks.coursesManage.addEventListener('click', (e) => {
        e.preventDefault();
        switchPanel('coursesManage');
    });

    sidebarLinks.studentsManage.addEventListener('click', (e) => {
        e.preventDefault();
        switchPanel('studentsManage');
    });

    // --- Course Form Management & Validation ---
    const courseForm = document.getElementById('course-form');
    const courseSuccessBanner = document.getElementById('course-success-banner');
    const courseErrorBanner = document.getElementById('course-error-banner');
    const courseErrorText = document.getElementById('course-error-text');
    const lessonDetailLinkContainer = document.getElementById('lesson-detail-link-container');
    const lessonDetailNavBtn = document.getElementById('lesson-detail-nav-btn');
    const btnCancelCourse = document.getElementById('btn-cancel-course');

    btnCancelCourse.addEventListener('click', (e) => {
        e.preventDefault();
        resetCourseForm();
        switchPanel('home');
    });

    courseForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        courseSuccessBanner.style.display = 'none';
        courseErrorBanner.style.display = 'none';

        const courseCode = document.getElementById('courseCode').value.trim();
        const startDate = document.getElementById('startDate').value;
        const courseName = document.getElementById('courseName').value.trim();
        const category = document.getElementById('category').value;
        const instructor = document.getElementById('instructor').value.trim();

        // --- Client-side Validations ---
        // Null / empty check
        if (!courseCode || !startDate || !courseName || !category || !instructor) {
            showCourseError("All fields are required and cannot be empty!");
            return;
        }

        // Course code pattern validation (alphanumeric)
        const alphanumericPattern = /^[A-Za-z0-9\-]+$/;
        if (!alphanumericPattern.test(courseCode)) {
            showCourseError("Course Code must be alphanumeric (only letters, numbers, and hyphens allowed)!");
            return;
        }

        // Valid date format check
        const datePattern = /^\d{4}-\d{2}-\d{2}$/;
        if (!datePattern.test(startDate)) {
            showCourseError("Start Date must be a valid date in YYYY-MM-DD format!");
            return;
        }

        const payload = {
            courseCode: courseCode,
            startDate: startDate,
            courseName: courseName,
            category: category,
            instructor: instructor
        };

        try {
            const response = await fetch('/api/courses', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });

            if (response.ok) {
                const savedCourse = await response.json();
                currentCourse = savedCourse;
                courseSuccessBanner.style.display = 'flex';
                lessonDetailLinkContainer.style.display = 'block';
                setFormDisabled(courseForm, true);
            } else {
                const errText = await response.text();
                showCourseError(errText || "Error saving course.");
            }
        } catch (error) {
            console.error('Error saving course:', error);
            showCourseError("Connection error. Could not connect to the server.");
        }
    });

    lessonDetailNavBtn.addEventListener('click', (e) => {
        e.preventDefault();
        if (currentCourse) {
            showLessonDetail(currentCourse);
        }
    });

    function showCourseError(msg) {
        courseErrorText.innerText = msg;
        courseErrorBanner.style.display = 'flex';
        courseErrorBanner.scrollIntoView({ behavior: 'smooth' });
    }

    function resetCourseForm() {
        courseForm.reset();
        setFormDisabled(courseForm, false);
        courseSuccessBanner.style.display = 'none';
        courseErrorBanner.style.display = 'none';
        lessonDetailLinkContainer.style.display = 'none';
        currentCourse = null;
    }

    function setFormDisabled(form, disabled) {
        const elements = form.elements;
        for (let i = 0; i < elements.length; i++) {
            if (elements[i].type !== 'submit' && !elements[i].className.includes('btn')) {
                elements[i].readOnly = disabled;
                elements[i].disabled = disabled;
            }
        }
        const submitBtn = form.querySelector('button[type="submit"]');
        if (submitBtn) submitBtn.disabled = disabled;
    }

    // --- Courses Management ---
    async function loadCourses() {
        const tableBody = document.querySelector('#courses-table tbody');
        tableBody.innerHTML = '<tr><td colspan="6" class="text-center">Loading courses...</td></tr>';

        try {
            const response = await fetch('/api/courses');
            const courses = await response.json();

            if (courses.length === 0) {
                tableBody.innerHTML = '<tr><td colspan="6" class="text-center py-3 text-muted">No courses found. Please add a course first.</td></tr>';
                return;
            }

            tableBody.innerHTML = '';
            courses.forEach(c => {
                const tr = document.createElement('tr');
                tr.innerHTML = `
                    <td>${c.courseCode}</td>
                    <td>${formatDate(c.startDate)}</td>
                    <td>${escapeHtml(c.courseName)}</td>
                    <td>${escapeHtml(c.category)}</td>
                    <td>${escapeHtml(c.instructor)}</td>
                    <td><a class="manage-lessons-link">Manage Lessons (Lesson Detail)</a></td>
                `;

                tr.querySelector('.manage-lessons-link').addEventListener('click', () => {
                    showLessonDetail(c);
                });

                tableBody.appendChild(tr);
            });
        } catch (error) {
            console.error('Error loading courses:', error);
            tableBody.innerHTML = '<tr><td colspan="6" class="text-center text-danger">Error loading courses!</td></tr>';
        }
    }

    // --- Lesson Detail Screen ---
    const lessonForm = document.getElementById('lesson-form');
    const lessonTitleText = document.getElementById('lesson-title-text');
    const lessonSuccessBanner = document.getElementById('lesson-success-banner');
    const lessonErrorBanner = document.getElementById('lesson-error-banner');
    const lessonErrorText = document.getElementById('lesson-error-text');
    const btnCancelLesson = document.getElementById('btn-cancel-lesson');

    btnCancelLesson.addEventListener('click', (e) => {
        e.preventDefault();
        lessonForm.reset();
        document.getElementById('lessonId').value = '';
        editingLessonId = null;
        lessonTitleText.innerText = 'Add/Edit Lesson Detail';
        lessonSuccessBanner.style.display = 'none';
        lessonErrorBanner.style.display = 'none';
        switchPanel('coursesManage');
    });

    function showLessonDetail(course) {
        currentCourse = course;
        editingLessonId = null;
        lessonTitleText.innerText = 'Add/Edit Lesson Detail';
        lessonForm.reset();
        document.getElementById('lessonId').value = '';
        lessonSuccessBanner.style.display = 'none';
        lessonErrorBanner.style.display = 'none';

        // Fill course summary card
        document.getElementById('summary-course-id').innerText = course.courseCode;
        document.getElementById('summary-start-date').innerText = formatDate(course.startDate);

        // Update list
        loadLessons();

        // Switch to lesson panel
        switchPanel('lessonDetail');
    }

    async function loadLessons() {
        const tableBody = document.querySelector('#lessons-table tbody');
        tableBody.innerHTML = '<tr><td colspan="6" class="text-center">Loading lessons...</td></tr>';
        document.getElementById('lessons-table-header').innerText = `List of Lessons for ${currentCourse.courseCode}`;

        try {
            const response = await fetch(`/api/lessons/course?courseCode=${currentCourse.courseCode}&startDate=${currentCourse.startDate}`);
            if (!response.ok) {
                throw new Error("Failed to load lessons");
            }
            const lessons = await response.json();

            if (lessons.length === 0) {
                tableBody.innerHTML = '<tr><td colspan="6" class="text-center py-3 text-muted">No lessons found. Please add a lesson using the form above.</td></tr>';
                return;
            }

            tableBody.innerHTML = '';
            lessons.forEach((l, index) => {
                const tr = document.createElement('tr');
                tr.innerHTML = `
                    <td>${index + 1}</td>
                    <td>${escapeHtml(l.lessonName)}</td>
                    <td>${l.duration} mins</td>
                    <td>${formatContentType(l.contentType)}</td>
                    <td><span class="badge ${getStatusBadgeClass(l.status)}">${formatStatus(l.status)}</span></td>
                    <td>
                        <a class="edit-lesson-link me-2">Edit</a>
                        <span style="color: #ccc;">/</span>
                        <a class="delete-lesson-link ms-2 delete-action">Delete</a>
                    </td>
                `;

                tr.querySelector('.edit-lesson-link').addEventListener('click', () => {
                    editLesson(l);
                });

                tr.querySelector('.delete-lesson-link').addEventListener('click', () => {
                    deleteLesson(l.id);
                });

                tableBody.appendChild(tr);
            });
        } catch (error) {
            console.error('Error loading lessons:', error);
            tableBody.innerHTML = '<tr><td colspan="6" class="text-center text-danger">Error loading lessons!</td></tr>';
        }
    }

    lessonForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        lessonSuccessBanner.style.display = 'none';
        lessonErrorBanner.style.display = 'none';

        const id = document.getElementById('lessonId').value;
        const lessonName = document.getElementById('lessonName').value.trim();
        const durationStr = document.getElementById('duration').value;
        const contentType = document.getElementById('contentType').value;
        const status = document.getElementById('status').value;

        // --- Client-side validations for Lesson ---
        // Null / empty validation
        if (!lessonName || !durationStr || !contentType || !status) {
            showLessonError("All fields are required!");
            return;
        }

        // Duration check (data type and value range check)
        const duration = parseInt(durationStr, 10);
        if (isNaN(duration) || duration <= 0) {
            showLessonError("Duration must be a positive integer!");
            return;
        }

        const payload = {
            id: id ? parseInt(id, 10) : null,
            lessonName: lessonName,
            duration: duration,
            contentType: contentType,
            status: status,
            course: {
                courseCode: currentCourse.courseCode,
                startDate: currentCourse.startDate
            }
        };

        try {
            const response = await fetch('/api/lessons', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });

            if (response.ok) {
                lessonForm.reset();
                document.getElementById('lessonId').value = '';
                editingLessonId = null;
                lessonTitleText.innerText = 'Add/Edit Lesson Detail';
                lessonSuccessBanner.style.display = 'flex';
                loadLessons();
            } else {
                const errText = await response.text();
                showLessonError(errText || "Error saving lesson.");
            }
        } catch (error) {
            console.error('Error saving lesson:', error);
            showLessonError("Connection error.");
        }
    });

    function showLessonError(msg) {
        lessonErrorText.innerText = msg;
        lessonErrorBanner.style.display = 'flex';
        lessonErrorBanner.scrollIntoView({ behavior: 'smooth' });
    }

    function editLesson(lesson) {
        editingLessonId = lesson.id;
        lessonTitleText.innerText = 'Edit Lesson Detail';
        lessonSuccessBanner.style.display = 'none';
        lessonErrorBanner.style.display = 'none';

        document.getElementById('lessonId').value = lesson.id;
        document.getElementById('lessonName').value = lesson.lessonName;
        document.getElementById('duration').value = lesson.duration;
        document.getElementById('contentType').value = lesson.contentType;
        document.getElementById('status').value = lesson.status;
    }

    async function deleteLesson(id) {
        if (confirm('Do you want to delete this lesson?')) {
            try {
                const response = await fetch(`/api/lessons/${id}`, {
                    method: 'DELETE'
                });

                if (response.ok) {
                    loadLessons();
                } else {
                    alert('Error deleting lesson!');
                }
            } catch (error) {
                console.error('Error deleting lesson:', error);
                alert('Connection error.');
            }
        }
    }

    // --- Helpers ---
    function formatDate(dateString) {
        if (!dateString) return '';
        const parts = dateString.split('-');
        if (parts.length === 3) {
            return `${parts[2]}/${parts[1]}/${parts[0]}`; // YYYY-MM-DD to DD/MM/YYYY
        }
        return dateString;
    }

    function formatContentType(type) {
        switch (type) {
            case 'Theory': return 'Lý thuyết';
            case 'Practice': return 'Thực hành';
            case 'Video': return 'Video';
            default: return type || '';
        }
    }

    function formatStatus(status) {
        switch (status) {
            case 'Draft': return 'Bản nháp';
            case 'Open': return 'Đang mở';
            case 'Locked': return 'Đã khóa';
            default: return status || '';
        }
    }

    function getStatusBadgeClass(status) {
        switch (status) {
            case 'Draft': return 'bg-secondary';
            case 'Open': return 'bg-success';
            case 'Locked': return 'bg-danger';
            default: return 'bg-primary';
        }
    }

    function escapeHtml(str) {
        if (!str) return '';
        return str
            .replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;')
            .replace(/"/g, '&quot;')
            .replace(/'/g, '&#039;');
    }
});
