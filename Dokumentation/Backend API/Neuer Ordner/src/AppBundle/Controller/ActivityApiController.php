<?php
/**
 * User: nsh
 * Date: 12.07.2017
 * Time: 10:34
 */

namespace AppBundle\Controller;


use FOS\RestBundle\Controller\Annotations as Rest;
use FOS\RestBundle\Controller\FOSRestController;
use Nelmio\ApiDocBundle\Annotation\ApiDoc;

/**
 * Class ApiActivityController
 * @package AppBundle\Controller
 * @Rest\RouteResource("activity")
 */
class ActivityApiController extends FOSRestController
{
    /**
     * Returns all Activities.
     * @ApiDoc(section="Activity", authentication=true, resource=true)
     * @return array
     */
    public function cgetAction()
    {
        return $this->get('doctrine.orm.default_entity_manager')
            ->getRepository('AppBundle:Activity')
            ->findAll();
    }
}