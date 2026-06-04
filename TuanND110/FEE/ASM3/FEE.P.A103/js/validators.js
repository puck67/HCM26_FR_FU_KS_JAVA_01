/**
 * Validation & sanitize — pure logic (FEE.P.A103 / PDF + project standards)
 */
(function (window) {
  'use strict';

  const LIMITS = {
    firstName: 20,
    lastName: 20,
    telephone: 11,
    email: 50,
    description: 200,
    searchKeyword: 50
  };

  const REGIONS = ['Europe', 'Africa', 'Australia', 'Asia', 'America'];
  const GENDERS = ['Male', 'Female'];
  const HOBBIES = ['Swimming', 'Cooking', 'Shopping', 'Sport', 'Dance', 'Sing'];

  const NAME_REGEX = /^[a-zA-ZÀ-ỹ\s'-]+$/;
  const TEL_REGEX = /^\d+$/;
  const EMAIL_REGEX = /^[a-zA-Z0-9]+@[a-zA-Z][a-zA-Z0-9]*\./;
  const HTML_TAG_REGEX = /<[^>]*>/g;

  function sanitizeText(value, maxLength) {
    const cleaned = String(value == null ? '' : value)
      .replace(HTML_TAG_REGEX, '')
      .replace(/[\x00-\x08\x0B\x0C\x0E-\x1F]/g, '')
      .trim();

    if (maxLength && cleaned.length > maxLength) {
      return cleaned.slice(0, maxLength);
    }
    return cleaned;
  }

  function sanitizeSearchKeyword(value) {
    return sanitizeText(value, LIMITS.searchKeyword);
  }

  function hasDigit(value) {
    return /\d/.test(value);
  }

  function validateFirstName(value) {
    const text = sanitizeText(value, LIMITS.firstName);
    if (!text) {
      return { valid: false, message: 'First name is required.', value: text };
    }
    if (text.length > LIMITS.firstName) {
      return { valid: false, message: 'First name must not exceed ' + LIMITS.firstName + ' characters.', value: text };
    }
    if (hasDigit(text) || !NAME_REGEX.test(text)) {
      return { valid: false, message: 'First name must be characters and not contain number.', value: text };
    }
    return { valid: true, message: '', value: text };
  }

  function validateLastName(value) {
    const text = sanitizeText(value, LIMITS.lastName);
    if (!text) {
      return { valid: false, message: 'Last name is required.', value: text };
    }
    if (text.length > LIMITS.lastName) {
      return { valid: false, message: 'Last name must not exceed ' + LIMITS.lastName + ' characters.', value: text };
    }
    if (hasDigit(text) || !NAME_REGEX.test(text)) {
      return { valid: false, message: 'Last name must be characters and not contain number.', value: text };
    }
    return { valid: true, message: '', value: text };
  }

  function validateTelephone(value) {
    const text = sanitizeText(value, LIMITS.telephone);
    if (!text) {
      return { valid: true, message: '', value: '' };
    }
    if (text.length > LIMITS.telephone) {
      return { valid: false, message: 'Telephone must not exceed ' + LIMITS.telephone + ' digits.', value: text };
    }
    if (!TEL_REGEX.test(text)) {
      return { valid: false, message: 'Telephone must be number.', value: text };
    }
    return { valid: true, message: '', value: text };
  }

  function validateEmail(value) {
    const text = sanitizeText(value, LIMITS.email);
    if (!text) {
      return { valid: true, message: '', value: '' };
    }
    if (text.length > LIMITS.email) {
      return { valid: false, message: 'Email must not exceed ' + LIMITS.email + ' characters.', value: text };
    }
    if (!EMAIL_REGEX.test(text)) {
      return { valid: false, message: 'Email must match format [a..Z][0..9]@[a..Z].', value: text };
    }
    return { valid: true, message: '', value: text };
  }

  function validateRegion(value) {
    if (!value || REGIONS.indexOf(value) === -1) {
      return { valid: false, message: 'You are in is required. Please select a region.', value: '' };
    }
    return { valid: true, message: '', value: value };
  }

  function validateGender(value) {
    const text = sanitizeText(value);
    if (!text || GENDERS.indexOf(text) === -1) {
      return { valid: false, message: 'Please select a valid gender.', value: 'Male' };
    }
    return { valid: true, message: '', value: text };
  }

  function validateDescription(value) {
    const text = sanitizeText(value, LIMITS.description);
    if (text.length > LIMITS.description) {
      return { valid: false, message: 'Description must not exceed ' + LIMITS.description + ' characters.', value: text };
    }
    return { valid: true, message: '', value: text };
  }

  function validateHobbies(list) {
    const safe = (list || []).filter(function (item) {
      return HOBBIES.indexOf(item) !== -1;
    });
    return { valid: true, message: '', value: safe };
  }

  function validateSearchKeyword(value, allowEmpty) {
    const text = sanitizeSearchKeyword(value);
    if (!text) {
      if (allowEmpty) {
        return { valid: true, message: '', value: '' };
      }
      return { valid: false, message: 'Please enter a keyword to search.', value: text };
    }
    return { valid: true, message: '', value: text };
  }

  function validatePersonPayload(payload) {
    const errors = [];
    const fieldErrors = {};
    const data = {};

    const checks = [
      { key: 'firstName', field: 'firstName', run: function () { return validateFirstName(payload.firstName); } },
      { key: 'lastName', field: 'lastName', run: function () { return validateLastName(payload.lastName); } },
      { key: 'gender', field: 'gender', run: function () { return validateGender(payload.gender); } },
      { key: 'telephone', field: 'telephone', run: function () { return validateTelephone(payload.telephone); } },
      { key: 'email', field: 'email', run: function () { return validateEmail(payload.email); } },
      { key: 'region', field: 'region', run: function () { return validateRegion(payload.region); } },
      { key: 'description', field: 'description', run: function () { return validateDescription(payload.description); } }
    ];

    checks.forEach(function (item) {
      const result = item.run();
      data[item.key] = result.value;
      if (!result.valid) {
        errors.push(result.message);
        fieldErrors[item.field] = result.message;
      }
    });

    const hobbiesResult = validateHobbies(payload.hobbies);
    data.hobbies = hobbiesResult.value;

    return {
      valid: errors.length === 0,
      errors: errors,
      fieldErrors: fieldErrors,
      data: data
    };
  }

  const FIELD_RUNNERS = {
    firstName: validateFirstName,
    lastName: validateLastName,
    telephone: validateTelephone,
    email: validateEmail,
    description: validateDescription,
    gender: validateGender
  };

  function validateFieldByName(fieldName, rawValue) {
    if (fieldName === 'region') {
      return validateRegion(rawValue);
    }
    const runner = FIELD_RUNNERS[fieldName];
    if (!runner) {
      return { valid: true, message: '', value: rawValue };
    }
    return runner(rawValue);
  }

  window.VisitorValidators = {
    LIMITS: LIMITS,
    REGIONS: REGIONS,
    sanitizeText: sanitizeText,
    sanitizeSearchKeyword: sanitizeSearchKeyword,
    validateFieldByName: validateFieldByName,
    validatePersonPayload: validatePersonPayload,
    validateSearchKeyword: validateSearchKeyword
  };
})(window);
