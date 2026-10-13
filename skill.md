---
name: sobhadinath-enterprise-website
description: Build and maintain the Sobhadi­nath Enterprise B2B packaging website with token-driven UI, accessible enquiry flows, and WhatsApp quote redirection.
---

# Sobhadi­nath Enterprise Web App Skill

Use the design tokens in `design.md`. Keep company data and product data near the top of `src/main.jsx` so the owner can update them without changing presentation code. Product prices default to “Request a quote”. Never invent certifications, testimonials, stock, dimensions, clients or capacity.

The primary conversion path is product selection → enquiry cart → validated details → review/WhatsApp redirect. WhatsApp URLs must be built dynamically with `encodeURIComponent`, target `https://wa.me/919284723868`, preserve line breaks, and never claim the message was sent before the customer sends it.
