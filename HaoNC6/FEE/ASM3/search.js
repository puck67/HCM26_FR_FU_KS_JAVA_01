$(document).ready(function () {

  // Load visitors from localStorage
  function getVisitors() {
    return JSON.parse(localStorage.getItem('visitors')) || [];
  }

  // Render rows into table
  function renderRows(rows) {
    var $tbody = $('#resultsBody');
    $tbody.empty();

    if (rows.length === 0) {
      $('#noResults').show();
      $('#searchHint').hide();
      $('#resultsTable').hide();
      return;
    }

    $('#noResults').hide();
    $('#searchHint').hide();
    $('#resultsTable').show();

    $.each(rows, function (i, v) {
      var row = '<tr>' +
        '<td>' + escapeHtml(v.firstName) + '</td>' +
        '<td>' + escapeHtml(v.lastName) + '</td>' +
        '<td>' + escapeHtml(v.gender) + '</td>' +
        '<td>' + escapeHtml(v.telephone) + '</td>' +
        '<td>' + escapeHtml(v.region) + '</td>' +
        '<td>' + escapeHtml(v.hobbies) + '</td>' +
        '<td>' + escapeHtml(v.description) + '</td>' +
        '</tr>';
      $tbody.append(row);
    });
  }

  // Escape HTML for safety
  function escapeHtml(str) {
    if (!str) return '';
    return String(str)
      .replace(/&/g, '&amp;')
      .replace(/</g, '&lt;')
      .replace(/>/g, '&gt;')
      .replace(/"/g, '&quot;');
  }

  // Search button click
  $('#btnSearch').on('click', function () {
    var keyword = $('#searchInput').val().trim().toLowerCase();
    var visitors = getVisitors();

    if (keyword === '') {
      // Show all if empty keyword
      renderRows(visitors);
      return;
    }

    // Search across ALL fields (not just first name)
    var filtered = visitors.filter(function (v) {
      return (
        (v.firstName   && v.firstName.toLowerCase().indexOf(keyword)   !== -1) ||
        (v.lastName    && v.lastName.toLowerCase().indexOf(keyword)    !== -1) ||
        (v.gender      && v.gender.toLowerCase().indexOf(keyword)      !== -1) ||
        (v.telephone   && v.telephone.toLowerCase().indexOf(keyword)   !== -1) ||
        (v.email       && v.email.toLowerCase().indexOf(keyword)       !== -1) ||
        (v.region      && v.region.toLowerCase().indexOf(keyword)      !== -1) ||
        (v.hobbies     && v.hobbies.toLowerCase().indexOf(keyword)     !== -1) ||
        (v.description && v.description.toLowerCase().indexOf(keyword) !== -1)
      );
    });

    renderRows(filtered);
  });

  // Also trigger search on Enter key
  $('#searchInput').on('keypress', function (e) {
    if (e.which === 13) {
      $('#btnSearch').trigger('click');
    }
  });

  // Seed some demo data if localStorage is empty (for demonstration)
  if (getVisitors().length === 0) {
    var demoData = [
      {
        firstName: 'John', lastName: 'Terry', gender: 'Male',
        telephone: '0909090909', email: 'john.terry@email.com',
        region: 'Europe', hobbies: 'Shopping, Cooking',
        description: 'One thing to note is you should make sure not to define the background color of a table cell in your stylesheet so that it stop the row highlight code from working properly.'
      },
      {
        firstName: 'John', lastName: 'Terry', gender: 'Male',
        telephone: '0909090909', email: 'john.terry2@email.com',
        region: 'Europe', hobbies: 'Shopping, Cooking',
        description: 'One thing to note is you should make sure not to define the background color of a table cell in your stylesheet so that it stop the row highlight code from working properly.'
      },
      {
        firstName: 'John', lastName: 'Terry', gender: 'Male',
        telephone: '0909090909', email: 'john.terry3@email.com',
        region: 'Europe', hobbies: 'Shopping, Cooking',
        description: 'One thing to note is you should make sure not to define the background color of a table cell in your stylesheet so that it stop the row highlight code from working properly.'
      }
    ];
    localStorage.setItem('visitors', JSON.stringify(demoData));
  }

  // On page load: hiển thị toàn bộ danh sách ngay lập tức
  renderRows(getVisitors());
});
