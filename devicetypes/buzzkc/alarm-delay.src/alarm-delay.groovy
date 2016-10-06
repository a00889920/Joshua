/**
 *  Simulated Alarm
 *
 *  Copyright 2014 SmartThings
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
metadata {
	definition (name: "Alarm Delay", namespace: "buzzkc", author: "BuzzKc") {
		capability "Alarm"
		capability "Sensor"
		capability "Actuator"
        
        preferences {
                section("Preferences"){
                        input "alamH", "capability.alarm", title:"Alarm" 
                        input "pauseMillis", "number", title: "Pause duration in Milliseconds:", required: true
                }
        }
	}

        

	simulator {
		// reply messages
		["strobe","siren","both","off"].each {
			reply "$it": "alarm:$it"
		}
	}

	tiles {
		standardTile("alarm", "device.alarm", width: 2, height: 2) {
			state "off", label:'off', action:'alarm.both', icon:"st.alarm.alarm.alarm", backgroundColor:"#ffffff"
			state "strobe", label:'strobe!', action:'alarm.off', icon:"st.alarm.alarm.alarm", backgroundColor:"#e86d13"
			state "siren", label:'siren!', action:'alarm.off', icon:"st.alarm.alarm.alarm", backgroundColor:"#e86d13"
			state "both", label:'alarm!', action:'alarm.off', icon:"st.alarm.alarm.alarm", backgroundColor:"#e86d13"
		}
		standardTile("strobe", "device.alarm", inactiveLabel: false, decoration: "flat") {
			state "default", label:'', action:"alarm.strobe", icon:"st.secondary.strobe", backgroundColor:"#cccccc"
		}
		standardTile("siren", "device.alarm", inactiveLabel: false, decoration: "flat") {
			state "default", label:'', action:"alarm.siren", icon:"st.secondary.siren", backgroundColor:"#cccccc"
		}       
		standardTile("off", "device.alarm", inactiveLabel: false, decoration: "flat") {
			state "default", label:'', action:"alarm.off", icon:"st.secondary.off"
		}
		main "alarm"
		details(["alarm","strobe","siren","test","off"])
	}
}

def installed() 
{
    subscribe(alamH,"alarm",alam_H) 
}

def alam_H(evt) {
}

def strobe() {
	sendEvent(name: "alarm", value: "strobe")
}

def siren() {
	sendEvent(name: "alarm", value: "siren")
}

def both() {
	sendEvent(name: "alarm", value: "both")

        pause( pauseMillis )
        alarmOn()
}

def off() {
	sendEvent(name: "alarm", value: "off")
}

// Parse incoming device messages to generate events
def parse(String description) {
	def pair = description.split(":")
	createEvent(name: pair[0].trim(), value: pair[1].trim())
}

def pause(millis) {
   def passed = 0
   def now = new Date().time
   log.debug "pausing... at Now: $now"
   /* This loop is an impolite busywait. We need to be given a true sleep() method, please. */
   while ( passed < millis ) {
       passed = new Date().time - now
   }
   log.debug "... DONE pausing."
}

def alarmOn() {
   alamH.both();
   def t = new Date()
   log.debug "ON at time $t"
}