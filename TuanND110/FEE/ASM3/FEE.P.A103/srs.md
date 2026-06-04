# FEE.P.A103 (HR) — Visitor Management

Aligned with **Bộ Tiêu Chí Phát Triển Dự Án (Vanilla HTML, CSS, JS) V2**.

## Structure

```
FEE.P.A103/
├── index.html
├── person.html
├── search.html
├── help.html
├── css/styles.css      # BEM + :root tokens
└── js/
    ├── validators.js   # Pure validation + sanitize
    ├── form-ui.js      # DOM feedback (classes only)
    ├── validation.js   # Person: blur, input, submit
    ├── data.js         # Storage + validate on add
    ├── pagination.js   # Event delegation on nav
    └── search.js       # Search validate + table
```

## Standards checklist

| Tiêu chí | Áp dụng |
|----------|---------|
| BEM | `visitor-form__`, `visitor-btn`, `visitor-panel--wide` |
| :root CSS vars | Colors + font family/size |
| Validate blur/input | Person + search keyword |
| Regex | Name, tel, email (PDF format) |
| HTML5 attrs | maxlength, required, pattern, aria-* |
| Errors under field | `visitor-form__error` + summary top |
| Semantic HTML | header, main, section, fieldset, legend |
| classList / classes | No inline style in JS |
| Event delegation | Form + pagination nav |
| const/let | All JS modules |
| Disable submit | Register + Search buttons |
| Sanitize | Strip HTML tags, trim, cap length |

## PDF validation

See `js/validators.js` — `validatePersonPayload`, `validateSearchKeyword`.
