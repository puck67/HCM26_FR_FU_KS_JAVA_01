/**
 * validate.js — client-side validation helpers
 * Used by all pages. No jQuery dependency for core logic.
 */

const Validator = (() => {

    const EMAIL_RE = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const PHONE_RE = /^[0-9]{9,13}$/;

    function setInvalid(el, msg) {
        el.classList.add('is-invalid');
        const fb = el.parentElement.querySelector('.invalid-feedback');
        if (fb) fb.textContent = msg;
        return false;
    }

    function setValid(el) {
        el.classList.remove('is-invalid');
        el.classList.add('is-valid');
        return true;
    }

    function clearState(el) {
        el.classList.remove('is-invalid', 'is-valid');
    }

    // Validate a single field by rules object
    // rules: { required, minLength, maxLength, email, phone, match }
    function validateField(el, rules = {}) {
        const val = el.value.trim();

        if (rules.required && val === '')
            return setInvalid(el, 'This field is required.');

        if (rules.minLength && val.length < rules.minLength)
            return setInvalid(el, `Minimum ${rules.minLength} characters.`);

        if (rules.maxLength && val.length > rules.maxLength)
            return setInvalid(el, `Maximum ${rules.maxLength} characters.`);

        if (rules.email && !EMAIL_RE.test(val))
            return setInvalid(el, 'Enter a valid email address.');

        if (rules.phone && !PHONE_RE.test(val))
            return setInvalid(el, 'Phone must be 9–13 digits.');

        if (rules.match) {
            const target = document.getElementById(rules.match);
            if (target && val !== target.value.trim())
                return setInvalid(el, 'Passwords do not match.');
        }

        return setValid(el);
    }

    // Validate entire form given a rules map { fieldId: rulesObj }
    function validateForm(rulesMap) {
        let valid = true;
        for (const [id, rules] of Object.entries(rulesMap)) {
            const el = document.getElementById(id);
            if (!el) continue;
            const ok = validateField(el, rules);
            if (!ok) valid = false;
        }
        return valid;
    }

    // Attach live validation on blur
    function attachLive(rulesMap) {
        for (const [id, rules] of Object.entries(rulesMap)) {
            const el = document.getElementById(id);
            if (!el) continue;
            el.addEventListener('blur', () => validateField(el, rules));
            el.addEventListener('input', () => clearState(el));
        }
    }

    return { validateForm, attachLive };
})();
