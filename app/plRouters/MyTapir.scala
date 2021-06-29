package plRouters

import sttp.tapir.Tapir
import sttp.tapir.json.play.TapirJsonPlay

/**
  * Created by luping.qiu in 4:56 PM 2021/6/29
  */
object MyTapir extends Tapir
  with TapirJsonPlay
