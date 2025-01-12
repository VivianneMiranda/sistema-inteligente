# sistema-inteligente

Este projeto implementa um ambiente inteligente utilizando Java 21. O ambiente é composto por um Gateway central, sensores simulados e um cliente que interage com o sistema. O foco é permitir a comunicação entre sensores e o Gateway, além de possibilitar que o cliente gerencie os dispositivos conectados.

## Estrutura do Projeto

### Componentes

1. **Gateway**
   - Recebe mensagens multicast dos sensores para registro e gerenciamento.
   - Responde a comandos TCP enviados pelo cliente para listar dispositivos ou alterar estados.
   
2. **Sensores**
   - Sensores simulados que enviam mensagens multicast periodicamente para o Gateway.
   - Exemplo implementado: Sensor de temperatura.

3. **Cliente**
   - Interface de linha de comando (CLI) que interage com o Gateway via TCP.
   - Possibilita o teste de conexão, listagem de dispositivos e envio de comandos.

---

## Funcionalidades

1. **Descoberta de Dispositivos**
   - Os sensores enviam mensagens multicast no formato:
     ```
     DEVICE_REGISTER:<id>:<type>:<state>
     ```
   - Exemplo: `DEVICE_REGISTER:temp1:temperature:25.0`

2. **Gerenciamento de Dispositivos**
   - O cliente pode listar dispositivos registrados no Gateway ou enviar comandos para alterar estados:
     - Comando: `COMMAND <id> <action>`
     - Exemplo: `COMMAND temp1 ligar`

3. **Comunicação**
   - **Multicast**: Sensores para Gateway.
   - **TCP**: Cliente para Gateway.

4. **Periodicidade**
   - Os sensores enviam atualizações multicast a cada 30 segundos.

---

## Tecnologias Utilizadas

- **Java 21**
- **Bibliotecas:**
  - `java.net` (Sockets TCP e multicast)
  - `java.io` (Entrada e saída de dados)
  - `java.util` (Manipulação de dados no Gateway)

---

## Como Executar

### Pré-requisitos

- Ambiente Java configurado (Java 21+)
- EC2 ou máquina com portas adequadas abertas para comunicação.

### Passos

1. **Compilar o Projeto**
   Compile todos os arquivos `.java`:
   ```bash
   javac -d out/ src/**/*.java
   ```
2. **Iniciar o Gateway**
   Execute o seguinte comando:
   ```bash
   java -cp out gateway.Gateway
   ```
3. **Executar Sensores**
   Por exemplo, para o sensor de temperatura:
   ```bash
   java -cp out devices.TemperatureSensor temp1 temperature 25.0
   ```
4. **Iniciar Cliente**
   Execute o cliente:
   ```bash
   java -cp out client.Client
   ```
---

---

## Mensagens

### Multicast (Sensores → Gateway)

- **Formato**:
   ```
   DEVICE_REGISTER:<id>:<type>:<state>
   ```
- **Descrição**: Enviado pelos sensores para o Gateway para registro e atualização de estado. 

### TCP (Cliente ↔ Gateway)

1. **Testar Conexão**
 - **Enviar**: 
   ```
   TEST_CONNECTION
   ```
 - **Resposta**: 
   ```
   Conexão bem-sucedida!
   ```

2. **Listar Dispositivos**
 - **Enviar**: 
   ```
   LIST_DEVICES
   ```
 - **Resposta**: 
   Lista de dispositivos conectados no formato:
   ```
   <ID>: <Tipo> - Estado
   ```
   Exemplo:
   ```
   temp1: temperature - Estado: 25.0
   ```

3. **Enviar Comandos**
 - **Enviar**: 
   ```
   COMMAND <id> <action>
   ```
 - **Exemplo**:
   ```
   COMMAND temp1 ligar
   ```
 - **Resposta**: 
   ```
   Comando recebido: COMMAND temp1 ligar
   ```
 Ou, em caso de erro:
   ```
   Erro: Dispositivo não encontrado.
   ```

---
