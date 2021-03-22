/**
 * LIFX Scene
 *
 * Copyright 2016 Eric Vitale
 *
 * Version 1.0.4 - Changed by iranintoavan. Changed from button to momentary switch. Still have issues where it won't show up in automation. Not sure why. (03/22/2021)
 * Version 1.0.3 - Fix to support the new SmartThings app. (08/20/2018)
 * Version 1.0.2 - Added the switch capability in order to use this device with Alexa (10/3/2017)
 * Version 1.0.1 - Converted to Async API (06/21/2017)
 * Version 1.0.0 - Initial Release (05/31/2017)
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

include 'asynchttp_v1'

metadata {
	definition (name: "LIFX Scene Switch", namespace: "iranintoavan", author: "iranintoavan") {
		capability "Actuator"
		capability "Switch"
		capability "Momentary"
		capability "Sensor"
        
        command "activateScene"
	}
    
    preferences {
       	section("LIFX Configuration") {
            input "token", "text", title: "API Token", required: true
            input "scene", "text", title: "Scene UUID", required: true
            input "defaultTransition", "decimal", title: "Default Transition Time", required: true, defaultValue: 1.0
        }
       
       	section("Settings") {
	        input "logging", "enum", title: "Log Level", required: false, defaultValue: "INFO", options: ["TRACE", "DEBUG", "INFO", "WARN", "ERROR"]
        }
    }

	// simulator metadata
	simulator {
	} 

	// UI tile definitions
	tiles(scale: 2){
		multiAttributeTile(name:"switch", type: "generic", width: 6, height: 4, canChangeIcon: true){
			tileAttribute("device.switch", key: "PRIMARY_CONTROL") {
				attributeState("off", label: 'Push', action: "momentary.push", backgroundColor: "#ffffff", nextState: "on")
				attributeState("on", label: 'Push', action: "momentary.push", backgroundColor: "#00a0dc")
			}	
		}
		main "switch"
		details "switch"
	}
}

private determineLogLevel(data) {
    switch (data?.toUpperCase()) {
        case "TRACE":
            return 0
            break
        case "DEBUG":
            return 1
            break
        case "INFO":
            return 2
            break
        case "WARN":
            return 3
            break
        case "ERROR":
        	return 4
            break
        default:
            return 1
    }
}

def log(data, type) {
    data = "LIFX -- ${device.label} -- ${data ?: ''}"
        
    if (determineLogLevel(type) >= determineLogLevel(settings?.logging ?: "INFO")) {
        switch (type?.toUpperCase()) {
            case "TRACE":
                log.trace "${data}"
                break
            case "DEBUG":
                log.debug "${data}"
                break
            case "INFO":
                log.info "${data}"
                break
            case "WARN":
                log.warn "${data}"
                break
            case "ERROR":
                log.error "${data}"
                break
            default:
                log.error "LIFX -- ${device.label} -- Invalid Log Setting of ${type}."
                log.error "Message = ${data}."
        }
    }
}

def parse(String description) {
}

def push() {
	activateScene(scene, defaultTransition)
    sendEvent(name: "switch", value: "on", isStateChange: true, displayed: false)
	sendEvent(name: "switch", value: "off", isStateChange: true, displayed: false)
	sendEvent(name: "momentary", value: "pushed", isStateChange: true)
}

def on() {
	push()
}

def off() {
	push()
}

def activateScene(uuid, duration=defaultTransition) {
	//commandLIFX(uuid, "PUT", "duration=${duration}")
    def command = ["duration": duration]
    commandLIFX(uuid, "PUT", command)
}

////////////   BEGIN LIFX COMMANDS ///////////
def commandLIFX(light, method, commands) {
    def rawURL = "https://api.lifx.com"
    def rawPath = ""
    
    rawPath = "/v1/scenes/scene_id:" + light + "/activate"
    
    def rawHeaders = ["Content-Type": "application/x-www-form-urlencoded", "Authorization": "Bearer ${token}"]
    
    def params = [
        uri: rawURL,
		path: rawPath,
		headers: rawHeaders,
        body: commands
    ]
    
    log("Full URL/Path = ${rawPath}.", "DEBUG")
    log("rawHeaders = ${rawHeaders}.", "DEBUG")
    log("body = ${commands}.", "DEBUG")
    
    asynchttp_v1.put('responseHandler', params)
}

def responseHandler(response, data) {

    if(response.getStatus() == 200 || response.getStatus() == 207) {
		log("Scene Activated.", "DEBUG")    
    } else {
    	log("Scene failed to activate. LIFX returned ${response.getStatus()}.", "ERROR")
    }
}
////////////// END LIFX COMMANDS /////////////
