

var exec = require('cordova/exec');

var LowLatencyAudio = {

	getCapabilities : function( success, fail ) {
		return exec(success, fail || this._emptyCallback, "LowLatencyAudio", "getCapabilities", []);
	},

	preloadFX: function ( id, assetPath, success, fail) {
		return exec(success || this._emptyCallback, fail || this._emptyCallback, "LowLatencyAudio", "preloadFX", [id, assetPath]);
	},    

	preloadAudio: function ( id, assetPath, voices, success, fail) {
		return exec(success || this._emptyCallback, fail || this._emptyCallback, "LowLatencyAudio", "preloadAudio", [id, assetPath, voices]);
	},

	play: function (id, success, fail) {
		return exec(success || this._emptyCallback, fail || this._emptyCallback, "LowLatencyAudio", "play", [id]);
	},

	stop: function (id, index, success, fail) {
		return exec(success || this._emptyCallback, fail || this._emptyCallback, "LowLatencyAudio", "stop", [id, index]);
	},

	loop: function (id, success, fail) {
		return exec(success || this._emptyCallback, fail || this._emptyCallback, "LowLatencyAudio", "loop", [id]);
	},

	unload: function (id, success, fail) {
		return exec(success || this._emptyCallback, fail || this._emptyCallback, "LowLatencyAudio", "unload", [id]);
	},

	setVolume: function(id, volume, success, fail) {
		return exec(success || this._emptyCallback, fail || this._emptyCallback, "LowLatencyAudio", "setVolume", [id, volume]);    
	},

	pause: function(id, index, success, fail) {
		return exec(success || this._emptyCallback, fail || this._emptyCallback, "LowLatencyAudio", "pause", [id, index]);        
	},

	getDuration: function(id, success, fail) {
		return exec(success, fail || this._emptyCallback, "LowLatencyAudio", "getDuration", [id]);    
	},

	getPosition: function(id, index, success, fail) {
		return exec(success, fail || this._emptyCallback, "LowLatencyAudio", "getPosition", [id, index]);    
	},

	_emptyCallback: function(){}

};

module.exports = LowLatencyAudio;