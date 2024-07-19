<h1 align="center">Library Software · IUT de Paris - Rives de Seine</h1>

The "Library Management Software" GitHub project is software that facilitates the reservation, borrowing and return of documents in a library setting. It manages these operations exclusively for registered library members, leveraging a client-server architecture for seamless interaction between users and the library's systems.

> [!IMPORTANT]  
> The project has been developed exclusively in a professional context, with the specific aim of promoting learning. It is carried out as a project for the University of Paris.

<p align="center">
  <img src="https://github.com/Corentin-Lcs/iut-library-management-software/blob/main/DesLibrary.png" alt="DesLibrary.png"/>
</p>

## Prerequisites

In order to facilitate the use of the project / system, I advise you to use `WampServer`, a Windows web development environment (i.e. WAMP-like).

WampServer includes three servers (Apache, MySQL and MariaDB), a script interpreter (PHP), as well as `phpMyAdmin` for web administration of MySQL databases (Source: [Wikipedia](https://en.wikipedia.org/wiki/WampServer)).

It has an administration interface to manage and administer its servers through a tray icon (icon near the Windows clock).

The `Visual C++ Redistributable Packages` are essential for the installation and proper functioning of WampServer because they provide the necessary libraries to run Apache, MySQL, and PHP extensions. Without these packages, WampServer could encounter compatibility and performance issues.

> [!NOTE]
> The installer for the Visual C++ Redistributable Packages is downloadable for both 32-bit and 64-bit systems in the [`prerequisites`](https://github.com/Corentin-Lcs/iut-library-management-software/tree/main/prerequisites) folder.

## Usage

Each type of operation is handled via a specific port on the server.

1. **Reservations**
   - **Port**: 3000
   - **Usage**: Document reservation requests are made remotely by subscribers. The reservation client sends a request containing the subscriber’s number and the desired document number. If the document is available, it is reserved for a defined duration.

2. **Borrowings**
   - **Port**: 4000
   - **Usage**: Borrowings are done on-site at the media library. The borrowing client sends a request with the subscriber’s number and the document number. The borrowing is validated if the document is available or reserved for the specific subscriber.

3. **Returns**
   - **Port**: 5000
   - **Usage**: Returns are also done on-site. The return client sends a request with the document number. The system verifies that the document was indeed borrowed and records the return.

Clients for each operation connect to the corresponding port and exchange the necessary data with the server service. Ensure that the server’s IP address is known and that each port is correctly configured to listen for specific requests.

## Project's Structure

```
iut-library-management-software/
├─ README.md
├─ LICENSE
├─ DesLibrary.png
├─ prerequisites/
│  └─ Visual C++ Redistributable Packages All-In-One x86 x64.exe
└─ src/
   ├─ .gitignore
   ├─ .idea/
   │  ├─ .gitignore
   │  ├─ dataSources.xml
   │  ├─ discord.xml
   │  ├─ libraries/
   │  │  ├─ apache_logging_log4j_core.xml
   │  │  ├─ fasterxml_jackson_core_databind.xml
   │  │  ├─ javax_mail.xml
   │  │  ├─ mysql_connector_java_5_1_49.xml
   │  │  └─ ojdbc6.xml
   │  ├─ material_theme_project_new.xml
   │  ├─ misc.xml
   │  ├─ modules.xml
   │  ├─ runConfigurations/
   │  │  ├─ BorrowService.xml
   │  │  ├─ ReservationService.xml
   │  │  └─ ReturnService.xml
   │  ├─ sqldialects.xml
   │  ├─ uiDesigner.xml
   │  └─ vcs.xml
   ├─ LICENSE
   ├─ brette_soft.iml
   ├─ config/
   │  ├─ database_config.json
   │  ├─ email_config.json
   │  ├─ server_config.json
   │  └─ timer_config.json
   ├─ resources/
   │  └─ log4j2.xml
   └─ src/
      ├─ application/
      │  ├─ clientlibrary/
      │  │  ├─ ClientFactory.java
      │  │  ├─ ClientManager.java
      │  │  └─ ClientStart.java
      │  ├─ clientuser/
      │  │  ├─ ClientFactory.java
      │  │  ├─ ClientManager.java
      │  │  └─ ClientStart.java
      │  └─ server/
      │     ├─ ServerStart.java
      │     ├─ configs/
      │     │  ├─ DatabaseConfig.java
      │     │  ├─ EmailConfig.java
      │     │  ├─ ServerConfig.java
      │     │  ├─ TimerConfig.java
      │     │  └─ components/
      │     │     ├─ ServerConfigType.java
      │     │     └─ TimeConfigType.java
      │     ├─ entities/
      │     │  ├─ BorrowException.java
      │     │  ├─ Document.java
      │     │  ├─ Entity.java
      │     │  ├─ ReservationException.java
      │     │  ├─ ReturnException.java
      │     │  ├─ Subscriber.java
      │     │  └─ types/
      │     │     ├─ DocumentEntity.java
      │     │     ├─ DocumentLogEntity.java
      │     │     ├─ DocumentState.java
      │     │     ├─ DvdEntity.java
      │     │     ├─ ReservationDocumentNotAvailableException.java
      │     │     ├─ SingleDocumentEntity.java
      │     │     └─ SingleSubscriberEntity.java
      │     ├─ factories/
      │     │  ├─ DataFactory.java
      │     │  ├─ DatabaseFactory.java
      │     │  ├─ EmailFactory.java
      │     │  ├─ ServerFactory.java
      │     │  └─ TimerFactory.java
      │     ├─ managers/
      │     │  ├─ ConfigurationManager.java
      │     │  ├─ DataManager.java
      │     │  ├─ DatabaseManager.java
      │     │  ├─ EmailManager.java
      │     │  ├─ MailReminderManager.java
      │     │  ├─ ServerManager.java
      │     │  └─ TimerManager.java
      │     ├─ models/
      │     │  ├─ Model.java
      │     │  └─ types/
      │     │     ├─ DocumentLogModel.java
      │     │     ├─ DocumentModel.java
      │     │     ├─ DvdModel.java
      │     │     └─ SubscriberModel.java
      │     ├─ services/
      │     │  ├─ ServiceUtils.java
      │     │  ├─ borrows/
      │     │  │  ├─ BorrowComponent.java
      │     │  │  ├─ BorrowServer.java
      │     │  │  └─ BorrowService.java
      │     │  ├─ reservations/
      │     │  │  ├─ ReservationComponent.java
      │     │  │  ├─ ReservationServer.java
      │     │  │  └─ ReservationService.java
      │     │  └─ returns/
      │     │     ├─ ReturnComponent.java
      │     │     ├─ ReturnServer.java
      │     │     └─ ReturnService.java
      │     ├─ sql/
      │     │  └─ scripts.sql
      │     └─ timers/
      │        ├─ ControllableTimer.java
      │        ├─ TimerTask.java
      │        └─ tasks/
      │           ├─ BorrowTask.java
      │           ├─ ReservationTask.java
      │           └─ UnbanUserTask.java
      └─ libraries/
         ├─ jackson/
         │  ├─ jackson-annotations-2.17.1.jar
         │  ├─ jackson-core-2.17.1.jar
         │  └─ jackson-databind-2.17.1.jar
         ├─ jdbc/
         │  ├─ mysql-connector-java-5.1.49.jar
         │  └─ ojdbc6.jar
         ├─ log4j/
         │  ├─ log4j-api-2.20.0.jar
         │  └─ log4j-core-2.20.0.jar
         ├─ mailing/
         │  ├─ javax.activation-1.2.0.jar
         │  └─ javax.mail.jar
         └─ server/
            ├─ Component.java
            ├─ Server.java
            ├─ Service.java
            ├─ SocketProtocolLink.java
            └─ communication/
               ├─ Protocol.java
               └─ StreamIOProtocol.java
```

## Meta

Created by [@Corentin-Lcs](https://github.com/Corentin-Lcs) and [@sevnx](https://github.com/sevnx). Feel free to contact me !

Distributed under the GNU GPLv3 license. See [LICENSE](https://github.com/Corentin-Lcs/iut-library-management-software/blob/main/LICENSE) for more information.
