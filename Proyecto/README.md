# 🗺️ Bogotravel – Diario Personal de Viajes en Bogotá

Repositorio del proyecto final de la materia **Ingeniería de Software 1** – Universidad Nacional de Colombia.  
Grupo: **Naruto Dattebayo 🍥**

---

## 📌 Descripción del Proyecto y Objetivo

**Bogotravel** es una aplicación de escritorio desarrollada en **Java 11+ con JavaFX** que permite a los usuarios llevar un registro personal de sus viajes y experiencias en lugares turísticos de la ciudad de Bogotá.

### 🎯 Objetivo
Ofrecer una solución **offline**, amigable y funcional para documentar viajes, subir fotos, planear próximas visitas y consultar estadísticas o recomendaciones basadas en la experiencia del usuario.

### 🔍 Diferenciación
- Enfoque en **experiencias locales y personales**.
- **100% funcional sin conexión a internet**.
- Organización de visitas y fotos por usuario.
- Integración con PostgreSQL y cifrado de contraseñas.

---

## 👥 Integrantes del Grupo

- [@adrianyebid](https://github.com/adrianyebid) – **Adrián Yebid Rincón** – ✉️ adrianyr@unal.edu.co
- [@Maicol-Ortiz](https://github.com/Maicol-Ortiz) – **Michael Andrés Ortiz Bernal**
- [@jruizgu](https://github.com/jruizgu) – **Juan Esteban Ruiz Guasca**
- [@Jhonny0523](https://github.com/Jhonny0523) – **Jhonatan Bolívar Laverde**

*Todos los integrantes están activos y colaborando en el desarrollo del proyecto 😐.*

---

## 🧩 Estructura del Proyecto
bogotravel/
├── controller/ # Controladores JavaFX (vista y lógica de UI)
├── dao/ # Acceso a datos y persistencia
├── db/ # Clase de conexión a PostgreSQL
├── model/ # Clases modelo (Usuario, Entrada, Lugar, etc.)
├── sesion/ # Gestión de sesión actual
├── resources/ # Archivos FXML (interfaz visual)
└── Main.java # Clase principal del proyecto


---

## 🛠️ Tecnologías utilizadas

| Herramienta       | Descripción                                     |
|-------------------|-------------------------------------------------|
| Java 11+          | Lenguaje principal                              |
| JavaFX            | Interfaz gráfica del sistema                    |
| PostgreSQL        | Base de datos relacional                        |
| JDBC              | Acceso a base de datos                          |
| Maven             | Gestión de dependencias                         |
| JBCrypt           | Cifrado de contraseñas                          |
| JUnit             | Pruebas unitarias de funcionalidad              |

---

## ⚙️ Requisitos de instalación y ejecución

### 🧬 Clona el proyecto

```bash
git clone https://github.com/adrianyebid/ingenieria-software1-grupo-naruto.git





