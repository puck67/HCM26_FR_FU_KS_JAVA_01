/**
 * Visitor data layer — sample records + localStorage (sanitized on save)
 */
(function (window) {
  'use strict';

  const STORAGE_KEY = 'fee_a103_visitors';

  const PDF_DESCRIPTION =
    'One thing to note is you should make sure not to define the background color of a table cell in your stylesheet as that\'ll stop the row highlight code from working properly.';

  function buildDefaultVisitors() {
    const pdfRow = {
      firstName: 'John',
      lastName: 'Terry',
      gender: 'Male',
      telephone: '0909090909',
      email: '',
      region: 'Europe',
      hobbies: ['Shopping', 'Cooking'],
      description: PDF_DESCRIPTION
    };

    const extras = [
      { firstName: 'Anna', lastName: 'Smith', gender: 'Female', telephone: '0912345678', email: 'anna@F.', region: 'Asia', hobbies: ['Swimming', 'Sport'], description: 'Team lead from Singapore office.' },
      { firstName: 'David', lastName: 'Brown', gender: 'Male', telephone: '0987654321', email: 'david@A.', region: 'America', hobbies: ['Cooking'], description: 'Visiting for annual review.' },
      { firstName: 'Emma', lastName: 'Wilson', gender: 'Female', telephone: '0900111222', email: 'emma@E.', region: 'Europe', hobbies: ['Dance', 'Sing'], description: 'Conference guest speaker.' },
      { firstName: 'Liam', lastName: 'Nguyen', gender: 'Male', telephone: '0933444555', email: '', region: 'Asia', hobbies: ['Shopping'], description: 'Vendor partner meeting.' },
      { firstName: 'Sophia', lastName: 'Martin', gender: 'Female', telephone: '0977888999', email: 'sophia@F.', region: 'Africa', hobbies: ['Sport', 'Cooking'], description: 'HR onboarding session.' },
      { firstName: 'Oliver', lastName: 'Clark', gender: 'Male', telephone: '0966554433', email: '', region: 'Australia', hobbies: ['Swimming'], description: 'Remote audit coordinator.' },
      { firstName: 'Mia', lastName: 'Lee', gender: 'Female', telephone: '0944332211', email: 'mia@A.', region: 'Asia', hobbies: ['Sing', 'Dance'], description: 'Design workshop participant.' },
      { firstName: 'Noah', lastName: 'Garcia', gender: 'Male', telephone: '0922113344', email: '', region: 'America', hobbies: ['Sport'], description: 'Client demo day visitor.' },
      { firstName: 'Ava', lastName: 'Taylor', gender: 'Female', telephone: '0955667788', email: 'ava@E.', region: 'Europe', hobbies: ['Shopping', 'Dance'], description: 'Training program observer.' },
      { firstName: 'Ethan', lastName: 'Moore', gender: 'Male', telephone: '0911223344', email: '', region: 'Africa', hobbies: ['Cooking', 'Sport'], description: 'Field research interview.' },
      { firstName: 'Isabella', lastName: 'Davis', gender: 'Female', telephone: '0999887766', email: 'isabella@A.', region: 'Australia', hobbies: ['Swimming', 'Sing'], description: 'Summer internship mentor visit.' },
      { firstName: 'James', lastName: 'Anderson', gender: 'Male', telephone: '0900123456', email: 'james@A.', region: 'America', hobbies: ['Sport', 'Shopping'], description: 'Security compliance briefing.' }
    ];

    return [pdfRow, pdfRow, pdfRow].concat(extras);
  }

  const DEFAULT_VISITORS = buildDefaultVisitors();

  function loadRegistered() {
    try {
      const raw = localStorage.getItem(STORAGE_KEY);
      return raw ? JSON.parse(raw) : [];
    } catch (e) {
      return [];
    }
  }

  function saveRegistered(list) {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(list));
  }

  function addVisitor(visitor) {
    if (!window.VisitorValidators) {
      return;
    }

    const result = window.VisitorValidators.validatePersonPayload(visitor);
    if (!result.valid) {
      return;
    }

    const list = loadRegistered();
    list.push(result.data);
    saveRegistered(list);
  }

  function getAllVisitors() {
    return DEFAULT_VISITORS.concat(loadRegistered());
  }

  function hobbiesToText(hobbies) {
    if (Array.isArray(hobbies)) {
      return hobbies.join(', ');
    }
    return hobbies || '';
  }

  window.VisitorData = {
    getAllVisitors: getAllVisitors,
    addVisitor: addVisitor,
    hobbiesToText: hobbiesToText
  };
})(window);
