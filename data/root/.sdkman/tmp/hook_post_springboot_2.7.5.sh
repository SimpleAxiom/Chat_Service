#!/bin/bash
#Post Hook: default-zip
function __sdkman_post_installation_hook {
    __sdkman_echo_debug "No Linux 64bit post-install hook found for Springboot 2.7.5."
    __sdkman_echo_debug "Moving $binary_input to $zip_output"
    mv -f "$binary_input" "$zip_output"
}