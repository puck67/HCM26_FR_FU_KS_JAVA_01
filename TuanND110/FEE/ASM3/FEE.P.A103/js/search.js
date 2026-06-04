/**
 * search.html — keyword validation, sanitize, search + pagination
 */
(function ($) {
  'use strict';

  const V = window.VisitorValidators;
  const UI = window.VisitorFormUI;

  let paginator = null;

  function visitorToRowText(visitor) {
    const hobbies = window.VisitorData
      ? window.VisitorData.hobbiesToText(visitor.hobbies)
      : (visitor.hobbies || '').toString();

    return [
      visitor.firstName,
      visitor.lastName,
      visitor.gender,
      visitor.telephone,
      visitor.email || '',
      visitor.region,
      hobbies,
      visitor.description
    ]
      .join(' ')
      .toLowerCase();
  }

  function filterVisitors(keyword) {
    const all = window.VisitorData ? window.VisitorData.getAllVisitors() : [];
    const q = (keyword || '').trim().toLowerCase();

    if (!q) {
      return all;
    }

    return all.filter(function (visitor) {
      return visitorToRowText(visitor).indexOf(q) !== -1;
    });
  }

  function renderTableRows(visitors) {
    const $tbody = $('#visitorTableBody');
    $tbody.empty();

    if (!visitors.length) {
      const $row = $('<tr></tr>');
      $row.append(
        $('<td colspan="7"></td>')
          .addClass('visitor-table__empty')
          .text('No visitors matched your search.')
      );
      $tbody.append($row);
      return;
    }

    visitors.forEach(function (v) {
      const hobbies = window.VisitorData
        ? window.VisitorData.hobbiesToText(v.hobbies)
        : v.hobbies;

      const $tr = $('<tr class="visitor-table__row"></tr>');
      $tr.append($('<td></td>').text(v.firstName));
      $tr.append($('<td></td>').text(v.lastName));
      $tr.append($('<td></td>').text(v.gender));
      $tr.append($('<td></td>').text(v.telephone));
      $tr.append($('<td></td>').text(v.region));
      $tr.append($('<td></td>').text(hobbies));
      $tr.append($('<td></td>').text(v.description));
      $tbody.append($tr);
    });
  }

  function validateSearchInput(allowEmpty) {
    const $input = $('#searchKeyword');
    const raw = $input.val();
    const result = V.validateSearchKeyword(raw, !!allowEmpty);

    UI.clearFieldState($input);
    UI.hideSummary($('#searchErrors'));

    if (!result.valid) {
      UI.setFieldError($input, result.message);
      UI.showSummary($('#searchErrors'), [result.message]);
      return null;
    }

    $input.val(result.value);
    return result.value;
  }

  function applyFilterAndPaginate(allowEmptyKeyword) {
    const keyword = validateSearchInput(allowEmptyKeyword);
    if (keyword === null) {
      return;
    }

    const $btn = $('#searchBtn');
    $btn.prop('disabled', true).addClass('visitor-btn--disabled');

    const filtered = filterVisitors(keyword);
    paginator.setItems(filtered);

    $btn.prop('disabled', false).removeClass('visitor-btn--disabled');
  }

  function handleSearchSubmit(e) {
    e.preventDefault();
    applyFilterAndPaginate(true);
  }

  $(document).ready(function () {
    paginator = new window.VisitorPaginator({
      infoSelector: '#paginationInfo',
      navSelector: '#paginationNav',
      pageSizeSelector: '#pageSize',
      defaultPageSize: 5,
      onPageChange: function (slice) {
        renderTableRows(slice);
      }
    });

    const all = window.VisitorData ? window.VisitorData.getAllVisitors() : [];
    paginator.setItems(all);

    $('#searchForm').on('submit', handleSearchSubmit);

    $('#searchForm').on('input', '#searchKeyword', function () {
      UI.clearFieldState($(this));
      UI.hideSummary($('#searchErrors'));
    });

    $('#searchForm').on('blur', '#searchKeyword', function () {
      const val = ($(this).val() || '').trim();
      if (val) {
        validateSearchInput();
      }
    });
  });
})(jQuery);
