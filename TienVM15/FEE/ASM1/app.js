/**
 * ==========================================================================
 * CMS CLIENT CORE ENGINE (app.js)
 * Enterprise-level frontend architecture, State Management, and routing.
 * ==========================================================================
 */

(function(window, $) {
    'use strict';

    // Application Global Namespace
    const CMSApp = {
        // State Management
        state: {
            currentUser: null,
            contents: [],
            activePage: null,
            isLoading: false
        },

        // Security utilities
        security: {
            escapeHtml: function(text) {
                if (typeof text !== 'string') return '';
                return text
                    .replace(/&/g, "&amp;")
                    .replace(/</g, "&lt;")
                    .replace(/>/g, "&gt;")
                    .replace(/"/g, "&quot;")
                    .replace(/'/g, "&#039;");
            }
        },

        // Universal Real-time Validator
        validator: {
            rules: {
                email: function(value) {
                    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                    return regex.test(value);
                },
                minLength: function(value, len) {
                    return value.length >= len;
                },
                maxLength: function(value, len) {
                    return value.length <= len;
                },
                range: function(value, min, max) {
                    const len = value.length;
                    return len >= min && len <= max;
                },
                phone: function(value) {
                    const regex = /^\d{9,13}$/;
                    return regex.test(value);
                }
            },

            validateField: function($input, checkFn, errorMsgId) {
                const isValid = checkFn($input.val());
                if (!isValid) {
                    $input.addClass('is-invalid').removeClass('is-valid');
                    $(errorMsgId).fadeIn(150);
                    return false;
                } else {
                    $input.addClass('is-valid').removeClass('is-invalid');
                    $(errorMsgId).fadeOut(150);
                    return true;
                }
            },

            shakeField: function($input) {
                $input.addClass('shake-input');
                setTimeout(() => {
                    $input.removeClass('shake-input');
                }, 400);
            }
        },

        // Storage Controller
        storage: {
            set: function(key, val) {
                try {
                    localStorage.setItem(key, JSON.stringify(val));
                } catch (e) {
                    console.error("Storage write error", e);
                }
            },
            get: function(key, fallback = null) {
                try {
                    const val = localStorage.getItem(key);
                    return val ? JSON.parse(val) : fallback;
                } catch (e) {
                    console.error("Storage read error", e);
                    return fallback;
                }
            },
            remove: function(key) {
                localStorage.removeItem(key);
            }
        },

        // AJAX Dynamic Router & Template Engine
        router: {
            load: function(pageUrl, menuIdToActive = null) {
                if (CMSApp.state.isLoading) return;
                CMSApp.state.isLoading = true;

                // Show Premium 5-Seconds loading overlay with progress countdown simulator
                const $overlay = $('#loading-overlay');
                const $progressText = $overlay.find('.loading-progress');
                $overlay.fadeIn(200);

                let progress = 0;
                $progressText.text('Loading (0s / 5s)...');
                
                const interval = setInterval(() => {
                    progress += 1;
                    $progressText.text(`Loading (${progress}s / 5s)...`);
                }, 1000);

                setTimeout(() => {
                    clearInterval(interval);

                    // Perform real jQuery AJAX request to fetch view layout
                    $.ajax({
                        url: pageUrl,
                        method: 'GET',
                        dataType: 'html',
                        cache: false,
                        success: function(htmlPayload) {
                            $('#main-content-area').hide().html(htmlPayload).fadeIn(300);
                            CMSApp.state.activePage = pageUrl;
                            
                            // Highlight sidebar
                            if (menuIdToActive) {
                                $('#sidebar ul li').removeClass('active');
                                $(menuIdToActive).addClass('active');
                            }
                        },
                        error: function(xhr, status, error) {
                            console.error(`AJAX error loading ${pageUrl}`, error);
                            $('#main-content-area').html(`
                                <div class="card shadow-sm border-0 bg-white">
                                    <div class="card-body text-center p-5">
                                        <i class="fas fa-exclamation-triangle text-danger fa-3x mb-3"></i>
                                        <h4 class="text-dark font-weight-bold">AJAX Request Failed</h4>
                                        <p class="text-muted">The requested template file <code>${pageUrl}</code> could not be fetched.</p>
                                        <button class="btn btn-primary mt-2" onclick="location.reload();">Reload Panel</button>
                                    </div>
                                </div>
                            `);
                        },
                        complete: function() {
                            CMSApp.state.isLoading = false;
                            $overlay.fadeOut(300);
                        }
                    });
                }, 5000); // Wait exactly 5 seconds
            }
        }
    };

    // Expose CMSApp API
    window.CMSApp = CMSApp;

})(window, window.jQuery);
