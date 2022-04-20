#!/bin/bash
echo "Parando Imagens......................................................"
sudo docker-compose down

echo "Inicializando containers............................................."
sudo docker-compose up -d
if [ "$?" != "0" ]; then
    echo "Falha ao inicializar as images!"
    exit 999
fi

echo "Containers Inicializados!............................................"
sudo docker-compose ps
echo "Removendo imagens antigas............................................"
sudo docker image prune -a -f

