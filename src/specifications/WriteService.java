/* ******************************************************
 * Project alpha - Composants logiciels 2015.
 * Copyright (C) 2015 <Binh-Minh.Bui-Xuan@ens-lyon.org>.
 * GPL version>=3 <http://www.gnu.org/licenses/>.
 * $Id: specifications/WriteService.java 2015-03-11 buixuan.
 * ******************************************************/
package specifications;

import tools.Position;
import tools.Sound;
import tools.Sound.SOUND;

import java.util.ArrayList;

public interface WriteService {
  public void setChildPosition(Position p);
  public void setStepNumber(int n);
  public void setChildHealth(int i);
  public void setChildScore(int i);
  public void setEnemy(ArrayList<EnemyService> enemy);
  public void addEnemy(Position p);
  public void updateLevel();
  public void addBullet(Position p);
void setBullet(ArrayList<BulletService> bu);
void setLevelnbpop(int i);
void setLevelnbkill(int i);
void setSoundEffect(SOUND s);
void addLollipop(Position p);
void setLollipop(ArrayList<Position> l);
void setSnail(boolean b, Position p);
void SnailPick(boolean b);
}
