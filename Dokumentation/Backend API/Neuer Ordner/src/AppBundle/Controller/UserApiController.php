<?php
/**
 * User: nsh
 * Date: 07.07.2017
 * Time: 11:02
 */

namespace AppBundle\Controller;


use AppBundle\Entity\User;
use FOS\RestBundle\Controller\Annotations as Rest;
use FOS\RestBundle\Controller\FOSRestController;
use FOS\RestBundle\View\View;
use Nelmio\ApiDocBundle\Annotation\ApiDoc;


/**
 * Class UserApiController
 * @package AppBundle\Controller
 * @Rest\RouteResource(resource="User")
 */
class UserApiController extends FOSRestController
{
    /**
     * Gets a user by ID
     *
     * @param User $user
     * @return User
     * @ApiDoc(output="AppBundle\Entity\User", section="User", authentication=true, resource=true)
     * @Rest\View()
     */
    public function getAction(User $user)
    {
        return $user;
    }

    /**
     * @return User
     * @ApiDoc(output="User", section="User", authentication=true, resource=true)
     * @Rest\View()
     */
    public function getSelfAction()
    {
        return $this->getUser();
    }
}