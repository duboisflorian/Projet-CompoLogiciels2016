/* ******************************************************
 * Project alpha - Composants logiciels 2015.
 * Copyright (C) 2015 <Binh-Minh.Bui-Xuan@ens-lyon.org>.
 * GPL version>=3 <http://www.gnu.org/licenses/>.
 * $Id: specifications/ReadService.java 2015-03-11 buixuan.
 * ******************************************************/
package specifications;

import tools.Position;
import tools.Sound;

import java.util.ArrayList;

import data.Field;
import data.Level;

public interface ReadService {
  public Position getChildPosition();
  public int getStepNumber();
  public Field getField();
  public int getChildHealth();
  public int getChildScore();
  public ArrayList<EnemyService> getEnemy();
  public Level getLevel();
  public ArrayList<BulletService> getBullet();
}
