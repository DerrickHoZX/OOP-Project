# Abstract Engine (OOP Project)

## Overview
This project implements a **2D Abstract Engine** using **Java and libGDX**, developed to demonstrate core **Object-Oriented Programming (OOP)** concepts and engine-level system design.

The engine provides a **modular and extensible framework** for managing scenes, entities, movement, collisions, input/output, timing, and asset handling. All components are designed to be **domain-agnostic**, allowing the engine to be reused for different simulations without modifying core logic.

A lightweight demonstration is included to showcase the engine’s capabilities while keeping all context-specific behavior outside the engine core.

---

## Core Engine Features

### Scene Management
- Stack-based scene control (`setScene`, `pushScene`, `popScene`)
- Supports overlays such as pause scenes
- Clear lifecycle handling: `onEnter`, `update`, `render`, `onExit`

### Entity Management
- Centralized entity creation, update, and removal
- Supports both collidable and non-collidable entities

### Movement System
- Delta-time based movement updates
- Component-based movement handling

### Collision System
- Entity–entity and boundary collision detection
- Collision responses delegated via interfaces
- Easily extensible collision behavior

### Input & Output
- Centralized keyboard and mouse input handling
- Abstracted audio output
- Logging and debug support

### Asset Management
- Centralized loading and caching of assets
- Proper asset lifecycle management

### Time Management
- Shared delta-time provider for frame-based updates
- Consistent timing across subsystems

---

## Architecture Overview
The engine follows a **manager-based architecture**, where each major responsibility is encapsulated within a dedicated manager:

- `SceneManager` – controls scene transitions and delegates execution
- `EntityManager` – manages active entities
- `MovementManager` – updates entity positions
- `CollisionManager` – detects and resolves collisions
- `IOManager` – handles input, audio, and logging
- `AssetManager` – loads and caches assets
- `TimeManager` – provides frame timing

<img width="4362" height="1534" alt="Final oopUML-UML Diagram drawio (1)" src="https://github.com/user-attachments/assets/3e1a1ffa-03fa-48cf-9b14-df9b2cd6e72f" />


Scenes orchestrate per-frame execution by coordinating these shared managers, ensuring **clear separation of concerns**.

---

## Object-Oriented Design
This project demonstrates the following OOP principles:

- **Abstraction**  
  Core behaviors are defined using abstract classes and interfaces.

- **Encapsulation**  
  Internal states are hidden behind controlled public APIs.

- **Inheritance**  
  All concrete scenes extend a common abstract `Scene` base class.

- **Polymorphism**  
  Scene behavior varies dynamically based on the active scene.

- **Composition**  
  Entities are composed of reusable components rather than hard-coded logic.

---

## Demonstration
A small demonstration is included to showcase:
<img width="1986" height="2048" alt="image" src="https://github.com/user-attachments/assets/caf33ca8-7dc6-4d0f-8e51-440da3bbcc77" />
<img width="1986" height="2048" alt="image" src="https://github.com/user-attachments/assets/0261dcfb-fb94-4fa9-a298-64ea03a7df01" />
<img width="1986" height="2048" alt="image" src="https://github.com/user-attachments/assets/ef6174d4-5a71-4b04-ad81-e489461039d6" />

- Scene transitions
- Entity movement
- Collision handling
- Input and audio output

The demonstration logic is kept separate from the engine core to maintain reusability.

---

## How to Run
Ensure **Java 17+** is installed.

From the project root:
```bash
./gradlew lwjgl3:run
