# meeting_agenda
# Gerenciar e participar dessas sessões de votação
Neste microserviço é possível criar pautas, abrir sessões de votação em pautas, votar em pautas que estejam com sessão de votação aberta, vizualizar resultados de votação em pautas.
# Execução do serviço
### Para a correta execução do serviço a máquina em que ele será executado deve obedecer as seguintes dependências:
- Dependências
  - JDK 1.8
  - MongoDB 4.4.10
  - Maven command line (caso prefira gerar o jar utilizando a linha de comando ao invés de utilizar uma IDE como o STS)
  - Guia de instalação official: https://maven.apache.org/install.html
   - Um guia mais detalhado para o windows pode ser encontrado em https://mkyong.com/maven/how-to-install-maven-in-windows/
 ### Após a instalação das dependências devem ser executados os passos abaixo:
 - Clonar ou fazer o download do projeto: https://github.com/jeanyamada/metting_agenda.git
 - Setar as informações uri de conexão com o mongodb, no arquivo "application-localhost.yaml"
    - Guia detalhado: https://docs.mongodb.com/manual/reference/connection-string/
 - Gerar o jar do projeto utilizando uma IDE ou entrando na pasta raiz do projeto e executando o comando abaixo
  ```
  mvn clean package spring-boot:repackage
  ```
 - Executar o jar do projeto com o comando
  ```
  java -jar product-ms-1.0.0.jar e
  ```
