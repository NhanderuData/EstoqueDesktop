# EstoqDesktop  
## Estrutura  
├── .idea/                                     # Configurações do IntelliJ IDEA  
├── .mvn/wrapper/                              # Wrapper Maven (para garantir a versão correta do Maven)  
├── src/  
│   ├── main/  
│   │   ├── java/  
│   │   │   └── com/example/estoqdesktop/  
│   │   │       ├── controller/                # Controladores JavaFX (HelloController, SupplierController)  
│   │   │       ├── model/                     # Modelos de dados (Supplier)  
│   │   │       ├── service/                   # Lógica de negócio (SupplierService)  
│   │   │       └── MainApp.java               # Classe principal da aplicação  
│   │   └── resources/                         # Arquivos de recursos (FXML, CSS, imagens)  
│   │       └── com/example/estoqdesktop/  
│   │           └── Supplier-view.fxml         # Arquivo FXML para a interface de fornecedores  
├── .gitignore                                 # Arquivos e pastas a serem ignorados pelo Git  
├── mvnw                                       # Script Maven Wrapper para Linux/macOS  
├── mvnw.cmd                                   # Script Maven Wrapper para Windows  
└── pom.xml                                    # Configuração do projeto Maven e dependências  
