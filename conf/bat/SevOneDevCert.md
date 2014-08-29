The script bellow shows how you to use the tool end to end for performing SevOne device certifications. 
Step 0 - Download and install SNMP simulator 
If you want to certify real network devices you can skip steps 0, Step 4, Step 5 and Step 6). 
If not note you will have to have a way to simulate snmpwalks obtained from real network devices. 
sudo apt-get update
sudo apt-get install pip
sudo apt-cache search pip | grep python
sudo apt-get install python-pip
sudo pip search snmpsim
sudo pip install snmpsim
mkdir -p /usr/snmpsim/data


Step 2 Download the latest release of snmp2xml4j from here

https://github.com/iTransformers/snmp2xml4j/releases 

Step 3 Setup snmp2xml4j your environment 

export SNMP2XML4J=/home/vpro/Downloads/niau/snmp2xml
export PATH=$PATH:$SNMP2XML4J/bin

Step 4 If you use a simulator obtain a fully numeric SNMP walk from the real device. 

snmpwalk -c e8000 -v 2c -On 1.2.3.4:11161 1.3.6 > e8000.snmpwalk 

Step 5 Clean the walk from some messages that are not supported by snmpsim 

snmpwalk2snmpsim.sh e8000.snmpwalk /usr/snmpsim/data/e8000.snmpwalk

Step 6 Start snmpsim simulator 
nohup snmpsimd.py --agent-udpv4-endpoint=0.0.0.0:11161 & 

Check the nohup output for an output like that
-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
Configuring /usr/snmpsim/data/e8000.snmpwalk controller
SNMPv1/2c community name: e8000
SNMPv3 context name: f77866a84d217ab463d02b37ebb6f599
-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+

Step 7 Run the tool and create a xmlized snmpwalk 

snmp-walk.sh Walk -md $SNMP2XML4J/mibs -a localhost/11161 -c e8000 -v 2c -f huaweihwSecStatGlobal.xml -o "hwSecurity" 

Ensure that the xml output file exists huaweihwSecStatGlobal.xml 


Step 8 Convert the result 

./xsltTransformer.sh ../conf/xslt/devCert1.xslt huaweihwSecStatGlobal.xml huaweihwSecStatGlobal_interim.xml

./xsltTransformer.sh ../conf/xslt/devCert2.xslt huaweihwSecStatGlobal_interim.xml huaweihwSecStatGlobal.s10 Huawei
  
  
Step 9 Review the final result 

less huaweihwSecStatGlobal.s10 

Step 10 Tidy up the descriptions, the units and the maximum values. 
Of course if you need something more complex always contact SevOne support!
