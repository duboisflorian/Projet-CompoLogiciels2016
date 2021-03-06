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
import java.util.Date;

import data.Child;

public interface WriteService {
  public void setChildPosition(Position p);
  public void setStepNumber(int n);
  public void setChildHealth(int i);
  public void setChildScore(int i);
  public void setEnemy(ArrayList<EnemyService> enemy);
  public void addEnemy(Position p);
  public void updateLevel();
  public void addBullet(Position p);
  public void setBullet(ArrayList<BulletService> bu);
  public void setLevelnbpop(int i);
  public void setLevelnbkill(int i);
  public void setSoundEffect(SOUND s);
  public void addLollipop(Position p);
  public void setLollipop(ArrayList<Position> l);
  public void setSnail(boolean b, Position p);
  public void SnailPick(boolean b);
void setSnailDirection(int i);
void setHighscore(int highscore);
void setFin(Date fin);
void setDebut(Date debut);
void setChild(Child c);
}
