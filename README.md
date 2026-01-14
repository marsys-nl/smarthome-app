# Smarthome App
Repository for the Smarthome apps (Android &amp; iOS), which is an app used in conjunction of the Smarthome Hub, that allows users to control various smart devices in their home.

# Versioning
This project uses a form of [CalVer](https://calver.org/) for versioning. The version format is `YYYY.MM[.PP][-SNAPSHOT]`.
With `YYYY` being the year, `MM` being the month, `PP` being the patch number (optional), and `-SNAPSHOT` indicating a pre-release version.

# Purpose
The `smarthome-app` repository contains the **end-user (mobile) application bounded context** for the Smarthome system.
Its purpose is to model and support user interaction with the SmartHome environment while remaining decoupled from backend implementation details.

The app is not a thin UI layer. It owns its **own application and domain logic**, tailored to user-facing concerns such as interaction flows, UI state, and client-side decision-making.

Where appropriate, the app reuses **shared building blocks** from `smarthome-domain`, such as value objects and common domain concepts, without depending on backend-specific behavior.

# Architectural style
The `smarthome-app` follows **Hexagonal Architecture**:
- The **core** consists of application and domain logic specific to the (mobile) clients.
- **Inbound adapters** translate user interactions and platform events into application commands
- **Outbound adapters** handle communication with the Smarthome hub backend, local storage and platform services.

This structure ensures that UI frameworks, networking libraries, and platform APIs remain replaceable and isolated from core logic. 

# Domain boundaries (DDD)
From a Domain-Driven Design perspective:
- The (mobile) app represents a **separate bounded context**
- Its domain focuses on:
  - User interaction
  - UI-oriented state
  - Client-side workflows
- The app may **reuse shared elements** from `smarthome-domain` (e.g. identifiers, value objects)
- The app **does not own** or redefine core smarthome domain behaviour managed by the hub.

# Event-Driven interaction
The app communicates with the Smarthome hub in an **event-driven manner**:
- The hub is the authoritative source of domain events and state changes
- The app:
  - Sends commands or intents to the hub
  - Consumes events or projections published by the hub
- Local UI state may be derived from:
  - Received domain events
  - Client-side projections or view models

# Explicit boundaries
The app **must not**:
- Act as the source of truth for smart-home domain state
- Perform server-side orchestration or persistence
- Contain infrastructure logic belonging to the hub
- Introduce coupling to backend frameworks or storage models

It **may**:
- Maintain client-specific domain models
- Apply client-side rules related to presentation or interaction
- Translate between UI concepts and shared domain concepts

# Design principles
- Bounded contexts are explicit and respected
- Shared code is reused intentionally, not by convenience
- Core logic is isolated from UI frameworks and transport concerns
- The app remains resilient to network failures and partial connectivity
- Event-driven communication is favored over state synchronization

# Contributing
To ensure a smooth and productive collaboration, please follow the guidelines as stated in [CONTRIBUTING.md](CONTRIBUTING.md).

# Other

## Pruning local branches

To prune and delete local branches please run `git fetch -p && for branch in $(git branch -vv | grep ': gone]' | awk '{print $1}'); do git branch -D $branch; done` in the project root directory

## License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.