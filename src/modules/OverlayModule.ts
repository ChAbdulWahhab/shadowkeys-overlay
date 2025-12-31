import { NativeModules } from 'react-native';

const { OverlayModule } = NativeModules;

interface OverlayModuleInterface {
    canDrawOverlays(): Promise<boolean>;
    openOverlaySettings(): void;
    startService(): void;
    stopService(): void;
    isAccessibilityServiceEnabled(): Promise<boolean>;
    openAccessibilitySettings(): void;
    sendInput(text: string | null, command: string | null): void;
    toggleKeyboard(): void;
}

export default OverlayModule as OverlayModuleInterface;
