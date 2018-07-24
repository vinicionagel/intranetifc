#!/bin/sh

echo "Apagando base de dados"
sudo -u postgres psql -c "drop database intranetifc;"

echo "Criando nova base de dados"
sudo -u postgres psql -c "create database intranetifc;"

echo "Criando tabelas ultima versao Completa"
sudo -u postgres psql intranetifc -f scriptBD.sql

echo "Criado pastas do sistemas"
sudo mkdir /opt/intranet

echo "Setando permissoes"
sudo chmod 777 -R /opt/intranet/
