import { AppRegistry, NativeModules } from 'react-native';
import App from './App';
import { name as appName } from './app.json';
import KeyboardOverlay from './src/components/KeyboardOverlay';

const { OverlayModule } = NativeModules;

const OverlayWrapper = () => {
    return (
        <KeyboardOverlay
            onKeyPress={key => OverlayModule.sendInput(key, null)}
            onCommandPress={command => OverlayModule.sendInput(null, command)}
            onClose={() => OverlayModule.toggleKeyboard()}
        />
    );
};

AppRegistry.registerComponent(appName, () => App);
AppRegistry.registerComponent('OverlayKeyboard', () => OverlayWrapper);
