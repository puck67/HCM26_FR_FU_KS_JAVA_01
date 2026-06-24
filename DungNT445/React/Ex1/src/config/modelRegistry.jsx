import React from 'react';
import { CalendarIcon, StudentIcon, TeacherIcon } from '../components/Icons';

// Common badge renderer for statuses
const getStatusBadge = (val) => {
  const valStr = String(val);
  let badgeClass = 'badge badge-default';
  if (['completed', 'active', 'done'].includes(valStr.toLowerCase())) {
    badgeClass = 'badge badge-success';
  } else if (['pending', 'inactive', 'progress'].includes(valStr.toLowerCase())) {
    badgeClass = 'badge badge-warning';
  }
  return <span className={badgeClass}>{valStr}</span>;
};

export const modelRegistry = {
  tasks: {
    key: 'tasks',
    label: 'Tasks Management',
    icon: <CalendarIcon />,
    columns: [
      { key: 'title', label: 'Task Title' },
      { key: 'dueDate', label: 'Due Date' },
      { key: 'status', label: 'Status', render: getStatusBadge }
    ],
    fields: [
      { name: 'title', label: 'Task Title', type: 'text', required: true },
      { name: 'dueDate', label: 'Due Date', type: 'date', required: true },
      { name: 'status', label: 'Status', type: 'select', options: ['Pending', 'Completed'], required: true, defaultValue: 'Pending' }
    ],
    initialData: [
      { id: 1, title: "Review Code Architecture", dueDate: "2026-06-25", status: "Completed" },
      { id: 2, title: "Deploy to Production Server", dueDate: "2026-06-28", status: "Pending" },
      { id: 3, title: "Optimize Web Performance", dueDate: "2026-07-02", status: "Pending" }
    ]
  },
  students: {
    key: 'students',
    label: 'Students Database',
    icon: <StudentIcon />,
    columns: [
      { key: 'name', label: 'Full Name' },
      { key: 'email', label: 'Email Address' },
      { key: 'class', label: 'Class' },
      { key: 'status', label: 'Status', render: getStatusBadge }
    ],
    fields: [
      { name: 'name', label: 'Full Name', type: 'text', required: true },
      { name: 'email', label: 'Email Address', type: 'email', required: true },
      { name: 'class', label: 'Class', type: 'text', required: true },
      { name: 'status', label: 'Status', type: 'select', options: ['Active', 'Inactive'], required: true, defaultValue: 'Active' }
    ],
    initialData: [
      { id: 1, name: "Nguyen Van A", email: "a.nguyen@school.edu", class: "12A1", status: "Active" },
      { id: 2, name: "Tran Thi B", email: "b.tran@school.edu", class: "11B2", status: "Active" },
      { id: 3, name: "Le Van C", email: "c.le@school.edu", class: "10C3", status: "Inactive" }
    ]
  },
  teachers: {
    key: 'teachers',
    label: 'Teachers Registry',
    icon: <TeacherIcon />,
    columns: [
      { key: 'name', label: 'Full Name' },
      { key: 'subject', label: 'Subject' },
      { key: 'department', label: 'Department' },
      { key: 'status', label: 'Status', render: getStatusBadge }
    ],
    fields: [
      { name: 'name', label: 'Full Name', type: 'text', required: true },
      { name: 'subject', label: 'Subject', type: 'text', required: true },
      { name: 'department', label: 'Department', type: 'text', required: true },
      { name: 'status', label: 'Status', type: 'select', options: ['Active', 'Inactive'], required: true, defaultValue: 'Active' }
    ],
    initialData: [
      { id: 1, name: "Dr. Pham Anh", subject: "Advanced Mathematics", department: "Science & Tech", status: "Active" },
      { id: 2, name: "Prof. Le Hoang", subject: "Computer Science", department: "IT", status: "Active" },
      { id: 3, name: "Ms. Nguyen Mai", subject: "English Literature", department: "Languages", status: "Inactive" }
    ]
  }
};
