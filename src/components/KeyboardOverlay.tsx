import React, { useState } from 'react';
import {
  View,
  Text,
  TouchableOpacity,
  StyleSheet,
  ScrollView,
  useWindowDimensions,
} from 'react-native';

interface KeyboardOverlayProps {
  onKeyPress: (key: string) => void;
  onCommandPress: (command: string) => void;
  onClose: () => void;
}

const KeyboardOverlay: React.FC<KeyboardOverlayProps> = ({
  onKeyPress,
  onCommandPress,
  onClose,
}) => {
  const [activeTab, setActiveTab] = useState<'keys' | 'cheats'>('keys');
  const { width, height } = useWindowDimensions();
  const isLandscape = width > height;

  const keys = [
    ['Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P'],
    ['A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L'],
    ['Z', 'X', 'C', 'V', 'B', 'N', 'M', 'DEL'],
    ['SPACE', 'ENTER'],
  ];

  const cheats = [
    { label: 'HEALTH & MONEY', code: 'HESOYAM' },
    { label: 'INF HEALTH', code: 'BAGUVIX' },
    { label: 'INF AMMO', code: 'FULLCLIP' },
    { label: 'WEAPONS 1', code: 'LXGIWYL' },
    { label: 'WEAPONS 2', code: 'PROFESSIONALSKIT' },
    { label: 'WEAPONS 3', code: 'UZUMYMW' },
    { label: 'WANTED UP', code: 'OSRBLHH' },
    { label: 'WANTED DOWN', code: 'ASNAEB' },
    { label: 'NEVER WANTED', code: 'AEZAKMI' },
    { label: 'SIX STAR WANTED', code: 'BRINGITON' },
    { label: 'SPAWN TANK', code: 'AIWPRTON' },
    { label: 'SPAWN JET', code: 'JUMPJET' },
    { label: 'SPAWN JETPACK', code: 'ROCKETMAN' },
    { label: 'FLYING CARS', code: 'CHITTYCHITTYBANGBANG' },
    { label: 'FLYING BOATS', code: 'FLYINGFISH' },
    { label: 'PERFECT HANDLING', code: 'STICKLIKEGLUE' },
    { label: 'SUPER JUMP', code: 'KANGAROO' },
    { label: 'SUPER PUNCH', code: 'STINGLIKEABEE' },
    { label: 'SPAWN HUNTER', code: 'OHDUDE' },
    { label: 'SPAWN MONSTER', code: 'MONSTERMASH' },
    { label: 'BLOW UP CARS', code: 'CPKTNWT' },
    { label: 'MAX MUSCLE', code: 'BUFFMEUP' },
    { label: 'MAX FAT', code: 'BTCDBCB' },
    { label: 'MAX SEX APPEAL', code: 'HELLOLADIES' },
    { label: 'NIGHT PROWLER', code: 'NIGHTPROWLER' },
    { label: 'SUNNY WEATHER', code: 'MGHXYRM' },
    { label: 'SANDSTORM', code: 'CWJXUOC' },
  ];

  const handleKeyTap = (key: string) => {
    if (key === 'DEL' || key === 'ENTER') {
      onCommandPress(key);
    } else if (key === 'SPACE') {
      onKeyPress(' ');
    } else {
      onKeyPress(key);
    }
  };

  return (
    <View style={[styles.container, { height: isLandscape ? 180 : 280 }]}>
      <View style={styles.header}>
        <View style={styles.tabs}>
          <TouchableOpacity
            onPress={() => setActiveTab('keys')}
            style={[styles.tab, activeTab === 'keys' && styles.activeTab]}>
            <Text style={[styles.tabText, activeTab === 'keys' && styles.activeTabText]}>KEYS</Text>
          </TouchableOpacity>
          <TouchableOpacity
            onPress={() => setActiveTab('cheats')}
            style={[styles.tab, activeTab === 'cheats' && styles.activeTab]}>
            <Text style={[styles.tabText, activeTab === 'cheats' && styles.activeTabText]}>GTA CHEATS</Text>
          </TouchableOpacity>
        </View>
        <TouchableOpacity onPress={onClose} style={styles.closeBtn}>
          <Text style={styles.closeBtnText}>HIDE ✕</Text>
        </TouchableOpacity>
      </View>

      <View style={styles.content}>
        {activeTab === 'keys' ? (
          <View style={styles.keyboard}>
            {keys.map((row, i) => (
              <View key={i} style={styles.row}>
                {row.map((key) => (
                  <TouchableOpacity
                    key={key}
                    onPress={() => handleKeyTap(key)}
                    style={[
                      styles.key,
                      key === 'SPACE' && styles.spaceKey,
                      key === 'ENTER' && styles.enterKey,
                      key === 'DEL' && styles.delKey,
                      isLandscape && styles.landscapeKey,
                    ]}>
                    <Text style={[styles.keyText, isLandscape && styles.landscapeKeyText]}>{key}</Text>
                  </TouchableOpacity>
                ))}
              </View>
            ))}
          </View>
        ) : (
          <ScrollView
            contentContainerStyle={[styles.cheatList, isLandscape && styles.landscapeCheatList]}
            horizontal={isLandscape}
          >
            {cheats.map((cheat) => (
              <TouchableOpacity
                key={cheat.code}
                onPress={() => onCommandPress(cheat.code)}
                style={[styles.cheatBtn, isLandscape && styles.landscapeCheatBtn]}>
                <View style={styles.cheatInfo}>
                  <Text style={styles.cheatLabel}>{cheat.label}</Text>
                  <Text style={styles.cheatCode}>{cheat.code}</Text>
                </View>
                <View style={styles.injectBadge}>
                  <Text style={styles.injectText}>INJECT</Text>
                </View>
              </TouchableOpacity>
            ))}
          </ScrollView>
        )}
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    backgroundColor: 'rgba(10,10,10,0.95)',
    borderTopLeftRadius: 20,
    borderTopRightRadius: 20,
    overflow: 'hidden',
    borderWidth: 1,
    borderColor: 'rgba(211, 47, 47, 0.3)',
  },
  header: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: 12,
    backgroundColor: '#111',
    borderBottomWidth: 1,
    borderBottomColor: '#222',
    height: 45,
  },
  tabs: {
    flexDirection: 'row',
    flex: 1,
  },
  tab: {
    paddingVertical: 12,
    paddingHorizontal: 15,
    borderBottomWidth: 3,
    borderBottomColor: 'transparent',
  },
  activeTab: {
    borderBottomColor: '#D32F2F',
  },
  tabText: {
    color: '#666',
    fontWeight: '900',
    fontSize: 10,
    letterSpacing: 0.5,
  },
  activeTabText: {
    color: '#D32F2F',
  },
  closeBtn: {
    padding: 8,
    backgroundColor: '#1a1a1a',
    borderRadius: 6,
    borderWidth: 1,
    borderColor: '#333',
  },
  closeBtnText: {
    color: '#FFF',
    fontSize: 10,
    fontWeight: '900',
  },
  content: {
    flex: 1,
    padding: 6,
  },
  keyboard: {
    flex: 1,
    justifyContent: 'center',
  },
  row: {
    flexDirection: 'row',
    justifyContent: 'center',
    marginBottom: 4,
  },
  key: {
    backgroundColor: '#181818',
    paddingVertical: 12,
    paddingHorizontal: 8,
    margin: 2,
    borderRadius: 6,
    minWidth: 34,
    alignItems: 'center',
    justifyContent: 'center',
    borderWidth: 1,
    borderColor: '#252525',
  },
  landscapeKey: {
    paddingVertical: 6,
    minWidth: 42,
  },
  spaceKey: {
    flex: 2,
    minWidth: 130,
    backgroundColor: '#202020',
  },
  enterKey: {
    backgroundColor: '#B71C1C',
    flex: 1,
    borderColor: '#D32F2F',
  },
  delKey: {
    backgroundColor: '#2A2A2A',
  },
  keyText: {
    color: '#EEE',
    fontSize: 13,
    fontWeight: 'bold',
  },
  landscapeKeyText: {
    fontSize: 11,
  },
  cheatList: {
    paddingBottom: 10,
    paddingHorizontal: 5,
  },
  landscapeCheatList: {
    flexDirection: 'row',
    paddingRight: 30,
    alignItems: 'center',
  },
  cheatBtn: {
    backgroundColor: '#151515',
    padding: 10,
    borderRadius: 8,
    marginBottom: 8,
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    borderWidth: 1,
    borderColor: '#333',
  },
  landscapeCheatBtn: {
    width: 160,
    marginRight: 10,
    marginBottom: 0,
    height: 65,
  },
  cheatInfo: {
    flex: 1,
  },
  cheatLabel: {
    color: '#FFF',
    fontSize: 11,
    fontWeight: '900',
  },
  cheatCode: {
    color: '#D32F2F',
    fontSize: 9,
    marginTop: 2,
    fontWeight: 'bold',
    opacity: 0.9,
  },
  injectBadge: {
    backgroundColor: 'rgba(211, 47, 47, 0.15)',
    paddingHorizontal: 8,
    paddingVertical: 5,
    borderRadius: 6,
    borderWidth: 1,
    borderColor: 'rgba(211, 47, 47, 0.3)',
  },
  injectText: {
    color: '#D32F2F',
    fontSize: 9,
    fontWeight: '900',
  },
});

export default KeyboardOverlay;
