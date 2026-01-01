import React, { useState, useEffect } from 'react';
import {
  SafeAreaView,
  StyleSheet,
  Text,
  View,
  TouchableOpacity,
  StatusBar,
  FlatList,
  Image,
} from 'react-native';
import OverlayModule from './src/modules/OverlayModule';
import KeyboardOverlay from './src/components/KeyboardOverlay';

const SetupStep = ({number, title, description, completed, onPress, icon}: any) => (
  <TouchableOpacity 
    style={[styles.card, completed && {borderColor: '#4CAF50'}]} 
    onPress={onPress}
    activeOpacity={0.7}
  >
    <View style={styles.cardHeader}>
      <Text style={styles.icon}>{completed ? '✅' : icon}</Text>
      <View style={styles.cardTitleContainer}>
        <Text style={styles.cardTitle}>{title}</Text>
        <Text style={styles.cardDescription}>{description}</Text>
      </View>
      <View style={[styles.cardAction, completed ? styles.actionActive : styles.actionInactive]}>
        <Text style={styles.actionText}>{completed ? 'OK' : 'FIX'}</Text>
      </View>
    </View>
  </TouchableOpacity>
);

const App = () => {
  const [overlayGranted, setOverlayGranted] = useState(false);
  const [accessibilityEnabled, setAccessibilityEnabled] = useState(false);
  const [isServiceRunning, setIsServiceRunning] = useState(false);
  const [keyboardEnabled, setKeyboardEnabled] = useState(false);

  useEffect(() => {
    checkPermissions();
    const interval = setInterval(checkPermissions, 2000);
    return () => clearInterval(interval);
  }, []);

  const checkPermissions = async () => {
    const overlay = await OverlayModule.canDrawOverlays();
    const accessibility = await OverlayModule.isAccessibilityServiceEnabled();
    const keyboard = await OverlayModule.isKeyboardEnabled();
    setOverlayGranted(overlay);
    setAccessibilityEnabled(accessibility);
    setKeyboardEnabled(keyboard);
    
    const running = await OverlayModule.isServiceRunning();
    setIsServiceRunning(running);
  };

  const toggleService = async () => {
    if (isServiceRunning) {
      OverlayModule.stopService();
      setIsServiceRunning(false);
    } else {
      if (!overlayGranted) {
        OverlayModule.openOverlaySettings();
        return;
      }
      OverlayModule.startService();
      setIsServiceRunning(true);
    }
  };

  return (
    <SafeAreaView style={styles.container}>
      <StatusBar barStyle="light-content" backgroundColor="#0A0A0A" />
      <View style={styles.header}>
        <Image
          source={{ uri: 'app_logo' }}
          style={styles.logo}
          defaultSource={{ uri: 'ic_launcher' }}
        />
        <Text style={styles.headerTitle}>SHADOW KEYS</Text>
        <Text style={styles.headerSubtitle}>PREMIUM CHEAT INJECTOR</Text>
      </View>

      <FlatList
        data={[1]}
        contentContainerStyle={styles.scrollContent}
        renderItem={() => (
          <View>
            <View style={styles.section}>
              <Text style={styles.sectionTitle}>Required Setup</Text>

              <SetupStep 
                icon="⌨️"
                title="Enable Cheat Keyboard"
                description="Enable 'Cheat Keyboard' in System Settings"
                completed={keyboardEnabled}
                onPress={() => OverlayModule.openKeyboardSettings()}
              />

              <SetupStep 
                icon="📱"
                title="Overlay Permission"
                description="Required for the floating cheat button"
                completed={overlayGranted}
                onPress={() => OverlayModule.openOverlaySettings()}
              />
              
              <SetupStep 
                icon="⚙️"
                title="Accessibility Service"
                description="Optional fallback for text injection"
                completed={accessibilityEnabled}
                onPress={() => OverlayModule.openAccessibilitySettings()}
              />
            </View>

            <View style={styles.section}>
              <Text style={styles.sectionTitle}>Service Control</Text>
              <TouchableOpacity
                style={[styles.mainButton, overlayGranted ? styles.startButton : styles.disabledButton]}
                onPress={toggleService}
              >
                <Text style={styles.mainButtonText}>
                  {isServiceRunning ? 'STOP SERVICE' : 'LAUNCH OVERLAY'}
                </Text>
              </TouchableOpacity>
              <Text style={styles.hint}>
                Tap the floating button in-game to open cheats.
              </Text>
            </View>

            <View style={styles.section}>
              <Text style={styles.sectionTitle}>Usage Guide</Text>
              <View style={styles.card}>
                <View style={{padding: 12}}>
                  <Text style={styles.cardDescription}>1. In-game, tap the floating button.</Text>
                  <Text style={[styles.cardDescription, {marginTop: 4}]}>2. Long-press button if keyboard picker is needed.</Text>
                  <Text style={[styles.cardDescription, {marginTop: 4}]}>3. Select 'Cheat Keyboard' to inject cheats.</Text>
                </View>
              </View>
            </View>
          </View>
        )}
        keyExtractor={(item) => item.toString()}
        ListFooterComponent={
          <View style={styles.footer}>
            <Text style={styles.footerLabel}>DEVELOPED BY</Text>
            <Text style={styles.developerName}>ABDUL WAHAB</Text>
            <Text style={styles.versionText}>v1.5.0 Gold Edition</Text>
          </View>
        }
      />
    </SafeAreaView>
  );
};

const PermissionItem = ({ title, status, onPress, description, icon }: any) => (
  <TouchableOpacity style={styles.card} onPress={onPress}>
    <View style={styles.cardHeader}>
      <Text style={styles.icon}>{icon}</Text>
      <View style={styles.cardTitleContainer}>
        <Text style={styles.cardTitle}>{title}</Text>
        <Text style={styles.cardDescription}>{description}</Text>
      </View>
      <View style={[styles.statusDot, status ? styles.statusActive : styles.statusInactive]} />
      <View style={[styles.cardAction, status ? styles.actionActive : styles.actionInactive]}>
        <Text style={styles.actionText}>{status ? 'OK' : 'GRANT'}</Text>
      </View>
    </View>
  </TouchableOpacity>
);

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#0A0A0A',
  },
  header: {
    paddingTop: 30,
    paddingBottom: 15,
    alignItems: 'center',
    backgroundColor: '#0F0F0F',
    borderBottomWidth: 1,
    borderBottomColor: '#330000',
  },
  logo: {
    width: 60,
    height: 60,
    borderRadius: 12,
    marginBottom: 10,
    borderWidth: 1,
    borderColor: '#333',
  },
  headerTitle: {
    fontSize: 24,
    fontWeight: '900',
    color: '#D32F2F',
  },
  headerSubtitle: {
    fontSize: 10,
    color: '#666',
    marginTop: 2,
    fontWeight: '700',
  },
  scrollContent: {
    padding: 15,
  },
  section: {
    marginBottom: 20,
  },
  sectionTitle: {
    fontSize: 12,
    color: '#D32F2F',
    fontWeight: 'bold',
    marginBottom: 10,
    textTransform: 'uppercase',
  },
  card: {
    backgroundColor: '#141414',
    borderRadius: 8,
    marginBottom: 10,
    overflow: 'hidden',
    borderWidth: 1,
    borderColor: '#222',
  },
  cardHeader: {
    flexDirection: 'row',
    padding: 12,
    alignItems: 'center',
  },
  icon: {
    fontSize: 20,
    marginRight: 12,
  },
  cardTitleContainer: {
    flex: 1,
  },
  cardTitle: {
    color: '#FFF',
    fontSize: 14,
    fontWeight: 'bold',
  },
  cardDescription: {
    color: '#888',
    fontSize: 10,
    marginTop: 1,
  },
  statusDot: {
    width: 6,
    height: 6,
    borderRadius: 3,
    marginRight: 10,
  },
  statusActive: {
    backgroundColor: '#4CAF50',
  },
  statusInactive: {
    backgroundColor: '#D32F2F',
  },
  cardAction: {
    paddingHorizontal: 12,
    paddingVertical: 6,
    borderRadius: 4,
  },
  actionActive: {
    backgroundColor: 'rgba(76, 175, 80, 0.1)',
  },
  actionInactive: {
    backgroundColor: 'rgba(211, 47, 47, 0.1)',
  },
  actionText: {
    fontSize: 10,
    fontWeight: 'bold',
    color: '#FFF',
  },
  mainButton: {
    padding: 15,
    borderRadius: 8,
    alignItems: 'center',
  },
  startButton: {
    backgroundColor: '#D32F2F',
  },
  disabledButton: {
    backgroundColor: '#1A1A1A',
  },
  mainButtonText: {
    color: '#FFF',
    fontWeight: '900',
    fontSize: 13,
  },
  secondaryBtn: {
    backgroundColor: '#141414',
    padding: 12,
    borderRadius: 8,
    alignItems: 'center',
    borderWidth: 1,
    borderColor: '#333',
  },
  secondaryBtnText: {
    color: '#FFF',
    fontWeight: 'bold',
    fontSize: 12,
  },
  testContainer: {
    height: 320,
    borderRadius: 8,
    overflow: 'hidden',
    marginTop: 10,
    borderWidth: 1,
    borderColor: '#333',
  },
  footer: {
    paddingVertical: 30,
    alignItems: 'center',
  },
  footerLabel: {
    color: '#444',
    fontSize: 10,
    fontWeight: 'bold',
  },
  developerName: {
    color: '#D32F2F',
    fontSize: 16,
    fontWeight: 'bold',
    marginTop: 2,
  },
  developerEmail: {
    color: '#666',
    fontSize: 10,
    marginTop: 2,
  },
  versionText: {
    color: '#333',
    fontSize: 9,
    marginTop: 10,
  },
  hint: {
    color: '#444',
    fontSize: 10,
    textAlign: 'center',
    marginTop: 8,
  },
});

export default App;
