# OPLUS Link Resolver

An Android app that resolves Android 16 **`downloadCheck`** links into their final direct download URLs.

This tool is designed for developers, testers, and enthusiasts who need to analyze or inspect OTA redirect links in a simple and clean way.

---

## How It Works

The app takes a **`downloadCheck` URL** as input, follows all HTTP redirects using proper request headers, and returns the **final resolved download link**.

It does **not download firmware** — it only **resolves publicly accessible redirect URLs**.

---

## Features

- Resolves shortened or redirected OTA URLs
- Handles multiple redirect chains
- Verifies whether the final result is a direct download link
- Clean and minimal Jetpack Compose UI
- Fast and lightweight
- No tracking, no ads

---

## Usage

1. Open the app
2. Paste your **`downloadCheck`** link
3. Tap **Resolve**
4. Copy or share the **final resolved URL**

---

## Author

Developed by **Abhinav Verma**

*   **GitHub:** [CodeSenseiX](https://github.com/CodeSenseiX)
*   **Telegram:** [@CodeSenseiX](https://t.me/CodeSenseiX)

---

## Donate

If you find this app helpful, you can support the developer through the following methods:

*   **PayPal:** [paypal.me/Abhinavftp](https://paypal.me/Abhinavftp?country.x=IN&locale.x=en_GB)
*   **UPI ID:** `abhinavftp98@axl`

---

## Built With

- Kotlin
- OkHttp
- Jetpack Compose

---

## Privacy Policy

- This app does not collect any personal data
- No tracking, analytics, or background data transmission
- No accounts, logins, or identifiers required

---

## Legal Compliance

- This project follows open-source software guidelines
- All third-party libraries (like OkHttp) are used under their respective open-source licenses
- The app does not bypass DRM, OEM security, or protected servers
- It only resolves publicly accessible redirect links

---

## Responsible Use Notice

This tool is intended for:

- Educational use
- Research & learning
- OTA testing
- Network redirection analysis

It is NOT intended for:

- Piracy
- Unauthorized firmware redistribution
- Bypassing OEM protection
- Commercial misuse

---

## Disclaimer
```
This application is provided 'for educational and research purposes only'.

- This app is not affiliated with, endorsed by, or supported by Google, Oppo, Realme, or any OEM
- The app does not host, store, or distribute firmware files
- The developer is not responsible for:
    - Data loss
    - Device damage
    - Flashing errors
    - Warranty void
- Users take full responsibility for actions performed using resolved links

```

## No Warranty
```
This application is provided '“as is” without warranty of any kind'.  
Use of this application is entirely 'at your own risk'.
```

## License
```
This project is licensed under the 'GNU General Public License v3.0 (GPL-3.0)'  
with the following additional conditions:

### Special License Conditions

1. Any 'copied, forked, or modified version' of this software 'must not be used for commercial, proprietary, or profit-making purposes'.

2. You 'may not publish or distribute' any copied or modified version of this app on any 'closed-source application distribution platforms', including but not limited to:
    - Google Play Store
    - Apple App Store
    - Any other proprietary app marketplace

3. All redistributed versions must:
    - Remain 'fully open-source'
    - Provide 'proper credit to the original author'
    - Retain the 'GPL v3.0 license and these additional conditions'

Violation of these terms will be treated as a 'license breach'.

```

---

## Badges
![License](https://img.shields.io/badge/License-GPL%20v3-blue.svg)
![Platform](https://img.shields.io/badge/Platform-Android-brightgreen.svg)
![Language](https://img.shields.io/badge/Language-Kotlin-blue.svg)
![UI](https://img.shields.io/badge/UI-Jetpack%20Compose-purple.svg)
![Networking](https://img.shields.io/badge/Network-OkHttp-orange.svg)

---

## Contributing

Contributions are welcome (non-commercial open-source only):

1. Fork the repository
2. Create a new branch
3. Commit your changes
4. Open a pull request

---

## Bug Reports

If you find a bug:

1. Describe the issue clearly
2. Share the input link used
3. Provide screenshots or logs if possible

---

## Support This Project

If this project helped you:

- Give it a **Star on GitHub**
- Support via donation
