🧑‍💼 Gerenciador de Funcionários

Este é um sistema desktop para o gerenciamento de funcionários, desenvolvido em Java com interface gráfica construída em JavaFX.
O projeto utiliza o Maven para gerenciamento de dependências e do ciclo de vida da build.

🚀 Funcionalidades

Cadastro de Funcionários — Permite adicionar novos funcionários com dados pessoais e de endereço.

Listagem e Consulta — Exibe todos os funcionários cadastrados e permite a busca por matrícula.

Exclusão — Permite remover um funcionário do sistema.

Persistência de Dados — Todas as informações são salvas em um arquivo funcionarios.csv, garantindo que os dados sejam mantidos após o fechamento da aplicação.

🧩 Pré-requisitos

Antes de executar o projeto, certifique-se de ter instalado:

☕ Java Development Kit (JDK)

Versão: 17 ou superior

É crucial que a versão 17 seja usada, pois o projeto foi configurado e testado com ela.

🧱 Apache Maven

Versão: 3.6 ou superior

O Maven geralmente já vem integrado em IDEs como IntelliJ e Eclipse, mas pode ser instalado separadamente.

💻 IDE (opcional, mas recomendado)

IntelliJ IDEA, Eclipse ou Visual Studio Code (com extensões para Java)

⚙️ Como Executar o Projeto

Você pode executar a aplicação de duas formas: via linha de comando (recomendado) ou diretamente pela sua IDE.

🧰 Método via IntelliJ IDEA

Abra o projeto no IntelliJ IDEA.

Verifique se ele foi reconhecido como um projeto Maven (a pasta src deve estar azul e o arquivo pom.xml reconhecido).

No canto direito da IDE, abra a aba Maven.

Navegue até:

[Nome do Projeto] > Plugins > javafx

Dê duplo clique em javafx:run.

⚠️ Importante:
Não execute o projeto clicando diretamente no botão "Play" (▶) do arquivo MainApp.java sem configurar as opções de VM, pois isso resultará no erro:

Error: JavaFX runtime components are missing


A forma correta (e mais simples) é sempre executar via mvn javafx:run.
