package spray.can.server.websockets.model

import akka.util.ByteString
import java.nio.ByteBuffer

class OpCode(val value: Byte, val isControl: Boolean)
/**
 * All the opcodes a frame can have, defined in the spec
 *
 * http://tools.ietf.org/html/rfc6455
 */
object OpCode{
  def find: PartialFunction[Int, OpCode] = {
    case 0 => Continuation
    case 1 => Text
    case 2 => Binary
    case 8 => ConnectionClose
    case 9 => Ping
    case 10 => Pong
  }

  case object Continuation extends OpCode(0, false)
  case object Text extends OpCode(1, false)
  case object Binary extends OpCode(2, false)
  case object ConnectionClose extends OpCode(8, true)
  case object Ping extends OpCode(9, true)
  case object Pong extends OpCode(10, true)

  val valid = List(Continuation, Text, Binary, ConnectionClose, Ping, Pong)
}


class CloseCode(val statusCode: Short){
  def toByteString = CloseCode.serializeCloseCode(statusCode)
}

/**
 * All the connection closing status codes, defined in the spec
 *
 * http://tools.ietf.org/html/rfc6455
 */
object CloseCode{
  /**
   * Converts a short into a bytestring to be used in the
   * CloseConnection frames.
   */
  def serializeCloseCode(code: Short) = ByteString(
    ByteBuffer.allocate(2)
      .putShort(code)
      .rewind().asInstanceOf[ByteBuffer]
  )

  object NormalClosure extends CloseCode(1000)
  object GoingAway extends CloseCode(1001)
  object ProtocolError extends CloseCode(1002)
  object UnsupportedData extends CloseCode(1003)

  object NoStatusReceived extends CloseCode(1005)
  object AbnormalClosure extends CloseCode(1006)
  object InvalidFramePayloadData extends CloseCode(1007)
  object PolicyViolation extends CloseCode(1008)
  object MessageTooBig extends CloseCode(1009)
  object MandatoryExt extends CloseCode(1010)
  object InternalServerError extends CloseCode(1011)
  object TlsHandshake extends CloseCode(1015)
  val statusCodes = Seq(
    NormalClosure,
    GoingAway,
    ProtocolError,
    UnsupportedData,
    NoStatusReceived,
    AbnormalClosure,
    InvalidFramePayloadData,
    PolicyViolation,
    MessageTooBig,
    MandatoryExt,
    InternalServerError,
    TlsHandshake
  ).map(_.statusCode)
}