$(document).ready(function () {
      
  // Hàm tính toán tổng quát sử dụng jQuery Iteration (.each) và Selectors
  function recalculateGalleryMetrics() {
    let totalStars = 0;
    let ratedCount = 0;
    let totalFavorites = 0;

    // Dùng bộ chọn jQuery và vòng lặp $.each() duyệt qua các card dữ liệu
    $('.image-card').each(function () {
      const rating = parseInt($(this).attr('data-rating')) || 0;
      const isFav = $(this).attr('data-favorite') === 'true';

      if (rating > 0) {
        totalStars += rating;
        ratedCount++;
      }
      if (isFav) {
        totalFavorites++;
      }
    });

    const newAvg = ratedCount > 0 ? (totalStars / ratedCount).toFixed(1) : "0.0";
    
    // Lấy giá trị hiện tại trên UI để kiểm tra xem có sự thay đổi hay không
    const currentAvg = $('#avgRatingValue').text();
    const currentFav = $('#totalFavValue').text();

    // Biến flag để chạy hoạt cảnh khi dữ liệu thực sự biến động
    let avgChanged = (currentAvg !== newAvg);
    let favChanged = (currentFav !== totalFavorites.toString());

    // Cập nhật text hiển thị số lượng item được rating
    $('#itemCountSub').text(`(based on ${ratedCount} item${ratedCount !== 1 ? 's' : ''})`);

    // Hiệu ứng Visual Feedback sử dụng .fadeIn() / .fadeOut() hoặc .animate() khi có thay đổi
    if (avgChanged) {
      $('#avgRatingValue').fadeOut(150, function () {
        $(this).text(newAvg).fadeIn(150);
      });
    }

    if (favChanged) {
      // Tạo hiệu ứng nhấp nháy chuyển màu cho icon trái tim ở footer để tăng độ sinh động
      if (totalFavorites > 0) {
        $('#footerHeartIcon').css('color', '#e74c3c');
      } else {
        $('#footerHeartIcon').css('color', '#555');
      }

      $('#totalFavValue').animate({ fontSize: '22px' }, 100, function () {
        $(this).text(totalFavorites).animate({ fontSize: '16px' }, 100);
      });
    }
  }

  /* ------------------------------------------------------------------------
     Event Handling: Sử dụng .on('click', ...) chuẩn đặc tả jQuery
     ------------------------------------------------------------------------ */

  // Xử lý Click chọn Star
  $('.image-card').on('click', '.rating-stars .fa-star', function () {
    const $thisStar = $(this);
    const $card = $thisStar.closest('.image-card');
    const selectedValue = parseInt($thisStar.data('value'));

    // Lưu thông tin rating vào thuộc tính data của Card
    $card.attr('data-rating', selectedValue);

    // Render trạng thái fill cố định các sao từ trái qua phải
    $thisStar.siblings().addBack().each(function () {
      const starVal = parseInt($(this).data('value'));
      if (starVal <= selectedValue) {
        $(this).addClass('filled');
      } else {
        $(this).removeClass('filled');
      }
    });

    recalculateGalleryMetrics();
  });

  // Xử lý Click Toggle Favorite (Áp dụng cho cả icon Heart và button Overlay)
  $('.image-card').on('click', '.heart-toggle, .btn-fav-overlay', function () {
    const $card = $(this).closest('.image-card');
    const isCurrentFav = $card.attr('data-favorite') === 'true';
    const nextFavState = !isCurrentFav;

    $card.attr('data-favorite', nextFavState ? 'true' : 'false');

    // Đồng bộ hóa trạng thái giao diện của nút Heart
    if (nextFavState) {
      $card.find('.heart-toggle').addClass('active');
    } else {
      $card.find('.heart-toggle').removeClass('active');
    }

    recalculateGalleryMetrics();
  });

  /* ------------------------------------------------------------------------
     Visual Effects: Hover effect "pre-fills" sử dụng addClass và removeClass
     ------------------------------------------------------------------------ */
  $('.image-card').on('mouseenter', '.rating-stars .fa-star', function () {
    const $hoveredStar = $(this);
    const currentHoverValue = parseInt($hoveredStar.data('value'));

    // Thêm class 'hovered' cho các sao nằm bên trái ngôi sao đang rải chuột qua
    $hoveredStar.siblings().addBack().each(function () {
      const starVal = parseInt($(this).data('value'));
      if (starVal <= currentHoverValue) {
        $(this).addClass('hovered');
      } else {
        $(this).removeClass('hovered');
      }
    });
  }).on('mouseleave', '.rating-stars', function () {
    // Khi chuột rời khỏi vùng tập hợp sao, xóa toàn bộ class 'hovered' trả lại trạng thái cũ
    $(this).find('.fa-star').removeClass('hovered');
  });

  // Khởi tạo tính toán ban đầu khi tải xong ứng dụng
  recalculateGalleryMetrics();
});
