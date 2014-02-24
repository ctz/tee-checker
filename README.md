TEE Checker
===========

This app aims to detect and report the existence of common Trusted Execution Environments (TEEs) on Android devices.

It currently detects:

* Whether the Android keystore reports use of a hardware root of trust (usually an indication of a TEE, but can be a SE too), and for what types of key.
* Giesecke & Devrient Mobicore / Trustonic t-base.
* Qualcomm SEE.
* Trusted Logic Trusted Foundations.

