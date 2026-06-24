# OneManage

[![Kotlin](https://img.shields.io/badge/kotlin-2.2.21-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![Ktor](https://img.shields.io/badge/ktor-3.3.2-orange.svg)](https://ktor.io/)
[![MongoDB](https://img.shields.io/badge/MongoDB-5.6.1-green.svg?logo=mongodb)](https://www.mongodb.com/)

OneManage is a comprehensive, security-hardened platform for centralized logging, modern application management, and distributed system monitoring. Built with **Ktor** and **Kotlin**, it offers high-performance ingestion and an intuitive analytics dashboard.
### [One Manage Dashbooard](https://onemanage.quest/)

## Features

- **Centralized Logging**: High-throughput log ingestion with support for batched transmissions and distributed tracing.
- **Advanced Analytics**: Real-time performance dashboards powered by MongoDB Aggregation Pipelines for lightning-fast metrics.
- **Security Hardened**:
    - **Password Protection**: Industry-standard **BCrypt** hashing.
    - **XSS Prevention**: Strict HTML sanitization using Jsoup for all user-generated content.
    - **Rate Limiting**: Protection against brute-force attacks on sensitive endpoints.
    - **Content Moderation**: Built-in filters to prevent explicit or abusive content hosting.
- **Privacy Policies**: Write, host, and securely publish unbranded public-facing privacy policies for your apps.
- **Remote Config**: Manage application feature flags dynamically without pushing app updates.
- **Generic Webhooks**: Proactive notification system for critical events like new bug reports or critical errors.
- **Firebase Analytics**: Seamless integration for tracking business events and application health.

## Security

Security is a core pillar of OneManage. We implement:
- **HTTP-Only/Secure Cookies**: Protecting session tokens from script access.
- **CSRF & XSS Protection**: Multi-layer defense for web-based dashboard fragments.
- **Cryptographic OTPs**: Using `SecureRandom` for unpredictable verification codes.

