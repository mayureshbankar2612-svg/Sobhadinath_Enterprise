# SOBHADINATH ENTERPRISE — Packaging Website Design System

## Mission
Deliver implementation-ready design-system guidance for a professional B2B PET food packaging marketing site.

## Style foundations
- Visual style: clean, trustworthy, industrial, implementation-first.
- Font: `DM Sans` for interface text and `Space Grotesk` for display headings.
- Semantic color tokens: `color.brand.primary=#1e7a52`, `color.brand.strong=#124734`, `color.brand.soft=#dcefe5`, `color.brand.accent=#b9e8c9`, `color.text.primary=#17231f`, `color.text.secondary=#68736d`, `color.surface.base=#f8faf7`, `color.surface.raised=#ffffff`, `color.border.default=#dbe4dd`.
- Spacing: use a 4px base scale; common values are 8px, 12px, 16px, 24px, 32px, 48px, 72px and 96px.
- Radius: 7px controls, 12px cards, 18px panels, 24px feature imagery, 999px floating actions.
- Shadows: `shadow.card=0 18px 44px rgba(18,71,52,.12)`; use only for elevation and focus clarity.

## Accessibility
- The site must target WCAG 2.2 AA.
- Interactive controls must have visible `:focus-visible` treatment.
- Text and controls must maintain readable contrast against their surfaces.
- Every form control must have a visible label and required fields must be validated.
- Keyboard users must be able to reach navigation, cart controls, modal close, and WhatsApp actions.

## Component rules
- Buttons must define default, hover, focus-visible, active, disabled and loading states.
- Cards should use semantic headings, descriptive image alternatives, and clear actions.
- Product cards must identify indicative placeholder photography when actual company images are not supplied.
- Enquiry forms must support long messages, multiple products, editable quantities, empty states and validation errors.
- Responsive layouts must work at mobile, tablet and desktop widths without horizontal overflow.
- WhatsApp actions must explain that the final message is sent by the customer inside WhatsApp.

## Content standards
- Copy should be concise, factual and B2B-oriented.
- The site must not invent certifications, reviews, client logos, production capacity, stock, prices or business claims.
- Unknown specifications must use “Request specification” or “Request a quote”.

## QA checklist
- Verify keyboard navigation and visible focus.
- Verify mobile navigation and responsive card layout.
- Verify the enquiry count, quantity updates, removal and empty state.
- Verify required-field validation and encoded WhatsApp line breaks.
- Verify all WhatsApp links target `919284723868`.
- Verify the build completes and no broken placeholders or dead buttons remain.
