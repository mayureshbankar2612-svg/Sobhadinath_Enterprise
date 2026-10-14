# Android TWA deployment

This project is a Vite/React single-page application, not a Next.js application. Its navigation is handled inside `src/main.jsx`, so no Next.js router configuration is required for the TWA.

## Public web assets

After deployment, the required URLs are:

- Manifest: `https://sobhadinathenterprise.netlify.app/manifest.webmanifest`
- 192x192 icon: `https://sobhadinathenterprise.netlify.app/icons/icon-192.png`
- 512x512 icon: `https://sobhadinathenterprise.netlify.app/icons/icon-512.png`
- Maskable 512x512 icon: `https://sobhadinathenterprise.netlify.app/icons/icon-maskable-512.png`

## Bubblewrap

Install Node.js, Java 17+, and the Android SDK/Build Tools on the Android build machine. Then run:

```bash
npm install -g @bubblewrap/cli
bubblewrap init --manifest=https://sobhadinathenterprise.netlify.app/manifest.webmanifest
bubblewrap build
bubblewrap install
```

Review the generated application ID, signing configuration, and displayed app name during `bubblewrap init`. Keep the signing keystore private and backed up.

For a Play Store release, upload the generated AAB from the Bubblewrap build output to Google Play Console. The web app must be deployed and the manifest/icons must be publicly reachable over HTTPS first.

## Digital Asset Links

`public/.well-known/assetlinks.json` has intentionally not been created. It must contain the SHA-256 fingerprint of the final Android signing certificate, and a placeholder fingerprint would be invalid. After signing is configured, publish the real file at:

`https://sobhadinathenterprise.netlify.app/.well-known/assetlinks.json`

## Service worker

No service worker is added. The website is an enquiry/WhatsApp application and does not currently have a safe offline data model. The TWA works without a service worker; adding offline caching later should be done only with an explicit cache and privacy strategy.
