/**
 * Reusable table pagination (FEE.P.A103)
 */
(function (window, $) {
  'use strict';

  const MAX_VISIBLE_PAGES = 5;

  function VisitorPaginator(options) {
    this.$info = $(options.infoSelector);
    this.$nav = $(options.navSelector);
    this.$pageSize = $(options.pageSizeSelector);
    this.onPageChange = options.onPageChange || function () {};
    this.items = [];
    this.currentPage = 1;
    this.pageSize = parseInt(options.defaultPageSize || 5, 10);
    this.bindEvents();
  }

  VisitorPaginator.prototype.bindEvents = function () {
    const self = this;

    this.$pageSize.on('change', function () {
      self.pageSize = parseInt($(this).val(), 10) || 5;
      self.currentPage = 1;
      self.emitChange();
    });

    this.$nav.on('click', '[data-page]', function (e) {
      e.preventDefault();
      const $btn = $(this);
      if ($btn.prop('disabled')) {
        return;
      }
      const page = $btn.data('page');
      if (page === 'first') {
        self.goTo(1);
      } else if (page === 'prev') {
        self.goTo(self.currentPage - 1);
      } else if (page === 'next') {
        self.goTo(self.currentPage + 1);
      } else if (page === 'last') {
        self.goTo(self.getTotalPages());
      } else {
        self.goTo(parseInt(page, 10));
      }
    });
  };

  VisitorPaginator.prototype.getTotalPages = function () {
    if (!this.items.length) {
      return 1;
    }
    return Math.ceil(this.items.length / this.pageSize);
  };

  VisitorPaginator.prototype.getSlice = function () {
    const start = (this.currentPage - 1) * this.pageSize;
    return this.items.slice(start, start + this.pageSize);
  };

  VisitorPaginator.prototype.goTo = function (page) {
    const total = this.getTotalPages();
    this.currentPage = Math.min(Math.max(1, page), total);
    this.emitChange();
  };

  VisitorPaginator.prototype.setItems = function (items) {
    this.items = items || [];
    this.currentPage = 1;
    this.emitChange();
  };

  VisitorPaginator.prototype.emitChange = function () {
    this.renderControls();
    this.onPageChange(this.getSlice(), this);
  };

  VisitorPaginator.prototype.renderControls = function () {
    const total = this.items.length;
    const totalPages = this.getTotalPages();
    const start = total ? (this.currentPage - 1) * this.pageSize + 1 : 0;
    const end = total ? Math.min(this.currentPage * this.pageSize, total) : 0;

    this.$info.text(
      total
        ? 'Showing ' + start + '–' + end + ' of ' + total + ' visitor(s) — Page ' + this.currentPage + ' / ' + totalPages
        : 'No visitors to display.'
    );

    this.$nav.empty();

    if (!total) {
      return;
    }

    const buttons = [
      { label: '«', page: 'first', disabled: this.currentPage === 1 },
      { label: '‹', page: 'prev', disabled: this.currentPage === 1 }
    ];

    const pages = this.buildPageNumbers(totalPages);
    pages.forEach(function (p) {
      if (p === '…') {
        buttons.push({ label: '…', page: null, disabled: true, ellipsis: true });
      } else {
        buttons.push({
          label: String(p),
          page: p,
          disabled: false,
          active: p === this.currentPage
        });
      }
    }, this);

    buttons.push(
      { label: '›', page: 'next', disabled: this.currentPage === totalPages },
      { label: '»', page: 'last', disabled: this.currentPage === totalPages }
    );

    buttons.forEach(function (btn) {
      const $li = $('<li class="visitor-pagination__item"></li>');
      const $b = $('<button type="button" class="visitor-pagination__btn"></button>')
        .text(btn.label)
        .prop('disabled', !!btn.disabled);

      if (btn.active) {
        $b.addClass('visitor-pagination__btn--active');
      }
      if (btn.page && !btn.ellipsis) {
        $b.attr('data-page', btn.page);
      }
      $li.append($b);
      this.$nav.append($li);
    }, this);
  };

  VisitorPaginator.prototype.buildPageNumbers = function (totalPages) {
    if (totalPages <= MAX_VISIBLE_PAGES) {
      return Array.from({ length: totalPages }, function (_, i) {
        return i + 1;
      });
    }

    const current = this.currentPage;
    const pages = [1];

    if (current > 3) {
      pages.push('…');
    }

    const rangeStart = Math.max(2, current - 1);
    const rangeEnd = Math.min(totalPages - 1, current + 1);

    for (let p = rangeStart; p <= rangeEnd; p++) {
      pages.push(p);
    }

    if (current < totalPages - 2) {
      pages.push('…');
    }

    pages.push(totalPages);
    return pages;
  };

  window.VisitorPaginator = VisitorPaginator;
})(window, jQuery);
