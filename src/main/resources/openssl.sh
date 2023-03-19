#!/bin/bash

dir=certs
if [[ ! -e $dir ]]; then
    mkdir $dir
elif [[ ! -d $dir ]]; then
    rm -rf $dir/*.pem
fi

#keypair
openssl genrsa -out certs/keypair.pem 2048
#public key
openssl rsa -in certs/keypair.pem -pubout --out certs/public.pem
#private key
openssl pkcs8 -topk8 -inform PEM -nocrypt -in certs/keypair.pem -out certs/private.pem