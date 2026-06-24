export interface Course {
    id: string;
    title: string;
    instructor: string;
    category: string;
    duration: string;
    level: "Beginner" | "Intermediate" | "Advanced";
    status: "Published" | "Draft";
}

export interface Lesson {
    id: string;
    courseTitle: string;
    title: string;
    duration: string;
    format: "Video" | "Article" | "Quiz";
}

export interface Student {
    id: string;
    name: string;
    email: string;
    joinedDate: string;
    status: "Active" | "Inactive";
}

export interface Enrollment {
    id: string;
    studentName: string;
    courseTitle: string;
    enrollmentDate: string;
    paymentStatus: "Paid" | "Pending" | "Refunded";
}